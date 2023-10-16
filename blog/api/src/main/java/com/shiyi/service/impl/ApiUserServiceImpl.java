package com.shiyi.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.common.ResultCode;
import com.shiyi.dto.EmailForgetPasswordDTO;
import com.shiyi.dto.EmailLoginDTO;
import com.shiyi.dto.EmailRegisterDTO;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.entity.*;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.enums.UserStatusEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.*;
import com.shiyi.service.ApiUserService;
import com.shiyi.service.EmailService;
import com.shiyi.service.RedisService;
import com.shiyi.utils.*;
import com.shiyi.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.shiyi.common.ResultCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {

    private final UserMapper userMapper;

    private final ArticleMapper articleMapper;

    private final CollectMapper collectMapper;

    private final RedisService redisService;

    private final UserInfoMapper userInfoMapper;

    private final FollowedMapper followedMapper;

    private final EmailService emailService;

    private final String[] userAvatarList = {"http://img.shiyit.com/avatars/buxie.png","http://img.shiyit.com/avatars/daizhi.png",
            "http://img.shiyit.com/avatars/fennu.png","http://img.shiyit.com/avatars/jingxi.png","http://img.shiyit.com/avatars/kaixin.png",
            "http://img.shiyit.com/avatars/shuanshuai.png"};


    /**
     * 邮箱登录
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseResult emailLogin(EmailLoginDTO vo) {

        User user = userMapper.selectNameAndPassword(vo.getEmail(), AesEncryptUtils.aesEncrypt(vo.getPassword()));
        if (user == null) {
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (user.getStatus() == UserStatusEnum.disable.code) {
            throw new BusinessException(DISABLE_ACCOUNT.desc);
        }

        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());

        //七天有效时间  登录
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));

        //组装数据
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .avatar(userInfo.getAvatar()).nickname(userInfo.getNickname())
                .intro(userInfo.getIntro()).webSite(userInfo.getWebSite()).email(user.getUsername())
                .loginType(user.getLoginType()).bjCover(userInfo.getBjCover()).token(StpUtil.getTokenValue()).build();

        return ResponseResult.success(userInfoVO);
    }

    /**
     * 判断用户是否微信登录成功
     *
     * @param loginCode 用户临时id
     * @return
     */
    @Override
    public ResponseResult wxIsLogin(String loginCode) {
        UserInfoVO user = (UserInfoVO) redisService.getCacheObject(RedisConstants.WX_LOGIN_USER + loginCode);
        if (user == null) {
            return ResponseResult.error("用户未被授权");
        }
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        user.setToken(StpUtil.getTokenValue());
        return ResponseResult.success(user);
    }

    /**
     * 微信扫码公众号登录
     * @param message
     * @return
     */
    @Override
    public String wechatLogin(WxMpXmlMessage message) {
        String content = message.getContent().toUpperCase();
        //先判断登录码是否已过期
        boolean loginFlag = redisService.hasKey(RedisConstants.WX_LOGIN_USER_STATUE + content);
        if (!loginFlag) {
            return "验证码已过期";
        }
        UserInfoVO userInfoVO = userMapper.selectByUserName(message.getFromUser());
        if (userInfoVO == null) {
            String ip = IpUtil.getIp();
            String ipSource = IpUtil.getIp2region(ip);

            // 保存用户信息
            UserInfo userInfo = UserInfo.builder()
                    .nickname("WECHAT-" + RandomUtils.generationCapital(6))
                    .avatar(userAvatarList[RandomUtils.generationNumber(userAvatarList.length)])
                    .build();
            userInfoMapper.insert(userInfo);
            // 保存账号信息
            User user = User.builder()
                    .userInfoId(userInfo.getId())
                    .username(message.getFromUser())
                    .password(AesEncryptUtils.aesEncrypt(message.getFromUser()))
                    .loginType(LoginTypeEnum.WECHAT.getType())
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ip)
                    .ipSource(ipSource)
                    .roleId(2)
                    .build();
            userMapper.insert(user);
            //组装返回信息
            userInfoVO = UserInfoVO.builder().id(user.getId()).loginType(user.getLoginType())
                    .avatar(userInfo.getAvatar()).email(userInfo.getEmail()).nickname(userInfo.getNickname())
                    .intro(userInfo.getIntro()).webSite(userInfo.getWebSite()).build();
        }


        //修改redis缓存 以便监听是否已经授权成功
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER + content, userInfoVO, 60, TimeUnit.SECONDS);
        return "网站登录成功！(若页面长时间未跳转请刷新验证码)";
    }

    /**
     * 获取微信公众号登录验证码
     *
     * @return
     */
    @Override
    public ResponseResult getWechatLoginCode() {
        String code = "DL" + RandomUtils.generationNumberChar(4);
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER_STATUE + code, false, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public ResponseResult selectUserInfo(String userId) {
        userId = StringUtils.isNotBlank(userId) ? userId : StpUtil.getLoginIdAsString();
        UserInfoVO userInfo = userInfoMapper.selectUserInfoByUserId(userId);
        return ResponseResult.success(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(UserInfoDTO vo) {
        UserInfo userInfo = BeanCopyUtils.copyObject(vo, UserInfo.class);
        userInfo.setId(vo.getUserInfoId());
        int update = userInfoMapper.updateById(userInfo);
        return ResponseResult.success("修改信息成功");
    }

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult selectUserInfoByToken(String token) {
        Object userId = StpUtil.getLoginIdByToken(token);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserIdNew(userId);
        return ResponseResult.success(userInfoVO);
    }

    /**
     * 第三方登录授权之后的逻辑
     *
     * @param response
     * @param source
     * @param httpServletResponse
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source,  HttpServletResponse httpServletResponse) throws IOException {
        String result = com.alibaba.fastjson.JSONObject.toJSONString(response.getData());
        log.info("第三方登录验证结果:{}", result);

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        Object uuid = jsonObject.get("uuid");
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp();
        String ipSource = IpUtil.getIp2region(ipAddress);
        // 判断是否已注册
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            // 更新登录信息
            userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));
        } else {
            // 获取第三方用户信息，保存到数据库返回
            // 保存用户信息
            UserInfo userInfo = UserInfo.builder()
                    .nickname(source.equals("github") ? jsonObject.get("username").toString() : jsonObject.get("nickname").toString())
                    .avatar(jsonObject.get("avatar").toString())
                    .build();
            userInfoMapper.insert(userInfo);
            // 保存账号信息
            user = User.builder()
                    .userInfoId(userInfo.getId())
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(com.shiyi.utils.DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.normal.getCode())
                    .build();
            userMapper.insert(user);
        }

        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect("http://www.shiyit.com/?token=" + StpUtil.getTokenValue());
    }

    @Override
    public ResponseResult sendEmailCode(String email) {
        try {
            emailService.sendCode(email);
            return ResponseResult.success();
        } catch (MessagingException e) {
            throw new BusinessException("发送邮件失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO) {
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailRegisterDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        UserInfoVO userInfoVO = userMapper.selectByUserName(emailRegisterDTO.getEmail());
        if (ObjectUtils.isNotEmpty(userInfoVO)) {
            throw new BusinessException("该邮箱账号已经注册");
        }
        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(emailRegisterDTO.getNickname())
                .email(emailRegisterDTO.getEmail())
                .build();
        userInfoMapper.insert(userInfo);
        // 保存账号信息
        User user = User.builder()
                .userInfoId(userInfo.getId())
                .username(emailRegisterDTO.getEmail())
                .password(AesEncryptUtils.aesEncrypt(emailRegisterDTO.getPassword()))
                .loginType(LoginTypeEnum.getType("email"))
                .roleId(2)
                .status(UserStatusEnum.normal.getCode())
                .build();
        userMapper.insert(user);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult forgetPassword(EmailForgetPasswordDTO emailForgetPasswordDTO) {
        //校验邮箱验证码
        boolean b = redisService.hasKey(RedisConstants.EMAIL_CODE + emailForgetPasswordDTO.getEmail());
        if (!b) {
            throw new BusinessException(ResultCode.ERROR_EXCEPTION_MOBILE_CODE);
        }
        User user = User.builder().password(AesEncryptUtils.aesEncrypt(emailForgetPasswordDTO.getPassword())).build();
        userMapper.update(user,new LambdaQueryWrapper<User>().eq(User::getUsername,emailForgetPasswordDTO.getEmail()));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getUserCount(String id) {
        id = StringUtils.isBlank(id) ? StpUtil.getLoginIdAsString() : id;
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getUserId, id));
        Integer collectCount = collectMapper.selectCount(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, id));
        Integer followedCount = followedMapper.selectCount(new LambdaQueryWrapper<Followed>().eq(Followed::getUserId, id));
        return ResponseResult.success().putExtra("articleCount", articleCount).putExtra("collectCount", collectCount)
                .putExtra("followedCount", followedCount);
    }

}
