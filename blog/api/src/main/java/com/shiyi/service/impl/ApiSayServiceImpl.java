package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.common.ResultCode;
import com.shiyi.entity.Say;
import com.shiyi.entity.SayComment;
import com.shiyi.exception.BusinessException;
import com.shiyi.handle.RelativeDateFormat;
import com.shiyi.mapper.SayCommentMapper;
import com.shiyi.mapper.SayMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.ApiSayService;
import com.shiyi.service.RedisService;
import com.shiyi.utils.IpUtil;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.ApiSayCommentVO;
import com.shiyi.vo.ApiSayVO;
import com.shiyi.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ApiSayServiceImpl implements ApiSayService {

    private final SayMapper sayMapper;
    private final RedisService redisService;

    private final UserMapper userMapper;

    private final SayCommentMapper sayCommentMapper;


    @Override
    public ResponseResult selectSayList() {

        //是否显示未公开的说说 登录用户id为1时显示所有说说
        boolean showPrivate = StpUtil.isLogin()  && StpUtil.getLoginIdAsString().equals("1");

        Page<ApiSayVO>  sayPage = sayMapper.selectPublicSayList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),showPrivate);
        for (ApiSayVO item : sayPage.getRecords()) {
            //获取点赞用户信息
            Set<Object> userIdList = redisService.getCacheSet(RedisConstants.SAY_LIKE_KEY + item.getId());
            List<UserInfoVO> likeUserList = new ArrayList<>();
            for (Object userId : userIdList) {
                UserInfoVO userInfoVO = userMapper.selectInfoByUserIdTwo(userId);
                likeUserList.add(userInfoVO);
            }
            item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime()));
            item.setUserLikeList(likeUserList);
            if (StpUtil.isLogin()){
                item.setIsLike(redisService.sIsMember(RedisConstants.SAY_LIKE_USER + StpUtil.getLoginIdAsString(), item.getId()));
            }
            List<SayComment> sayComments = sayCommentMapper.selectList(new LambdaQueryWrapper<SayComment>().eq(SayComment::getSayId, item.getId()));
            List<ApiSayCommentVO> sayCommentVOList = new ArrayList<>();
            for (SayComment sayComment : sayComments) {
                UserInfoVO userInfoVO = userMapper.selectInfoByUserIdTwo(sayComment.getUserId());
                ApiSayCommentVO apiSayCommentVO = ApiSayCommentVO.builder().userId(sayComment.getUserId()).nickname(userInfoVO.getNickname()).replyUserId(sayComment.getReplyUserId())
                        .content(sayComment.getContent()).ipAddress(sayComment.getIpAddress()).createTime(sayComment.getCreateTime()).build();
                if (StringUtils.isNotBlank(sayComment.getReplyUserId())) {
                    userInfoVO = userMapper.selectInfoByUserIdTwo(sayComment.getReplyUserId());
                    apiSayCommentVO.setReplyUserNickname(userInfoVO.getNickname());
                }
                sayCommentVOList.add(apiSayCommentVO);
            }
            item.setSayCommentVOList(sayCommentVOList);
        }

        return ResponseResult.success(sayPage);
    }

    @Override
    public ResponseResult like(String sayId) {
        String userId = StpUtil.getLoginIdAsString();
        // 判断是否点赞
        String sayLikeUser = RedisConstants.SAY_LIKE_USER + userId;
        String sayLike = RedisConstants.SAY_LIKE_KEY + sayId;
        if (redisService.sIsMember(sayLikeUser, sayId)) {
            // 点过赞则删除说说id
            redisService.sRemove(sayLikeUser, sayId);
            redisService.sRemove(sayLike, userId);
            return ResponseResult.success("取消点赞成功");
        }
            // 未点赞则增加说说id
            redisService.sAdd(sayLikeUser, sayId);
            // 说说点赞量+1
            redisService.sAdd(sayLike, userId);

            //构建通知消息
//            Article article = articleMapper.selectById(articleId);
//            SystemNoticeHandle.sendNotice(article.getUserId(), MessageConstant.MESSAGE_LIKE_NOTICE,MessageConstant.SYSTEM_MESSAGE_CODE,articleId,null,null);
        return ResponseResult.success("点赞成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult comment(SayComment sayComment) {
        sayComment.setUserId(StpUtil.getLoginIdAsString());
        sayComment.setIp(IpUtil.getIp());
        sayComment.setIpAddress(IpUtil.getIp2region(sayComment.getIp()));
        sayCommentMapper.insert(sayComment);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSay(Say say) {
        String userId = StpUtil.getLoginIdAsString();
        if (!userId.equals("1")) {
            throw new BusinessException(ResultCode.NO_PERMISSION);
        }
        say.setUserId(userId);
        sayMapper.insert(say);
        return ResponseResult.success();
    }
}
