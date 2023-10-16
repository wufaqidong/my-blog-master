package com.shiyi.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.common.Constants;
import com.shiyi.common.ResponseResult;
import com.shiyi.dto.Captcha;
import com.shiyi.dto.LoginDTO;
import com.shiyi.entity.User;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.LoginService;
import com.shiyi.utils.AesEncryptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.shiyi.common.ResultCode.ERROR_PASSWORD;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:59
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

    private final UserMapper userMapper;

    private final CaptchaServiceImpl captchaService;

    @Override
    public ResponseResult getCaptcha(Captcha captcha) {
        return ResponseResult.success(captchaService.getCaptcha(captcha));
    }

    @Override
    public ResponseResult login(LoginDTO dto) {

        //先校验验证码
        String msg = captchaService.checkImageCode(dto.getNonceStr(),dto.getValue());
        if (StringUtils.isNotBlank(msg)) {
            return ResponseResult.error(msg);
        }

        //校验用户名和密码
        User user = userMapper.selectNameAndPassword(dto.getUsername(), AesEncryptUtils.aesEncrypt(dto.getPassword()));
        if (user == null){
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (dto.getRememberMe()) {
            StpUtil.login(user.getId(),new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        }else {
            StpUtil.login(user.getId(),"system");
        }
        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));
        return ResponseResult.success(StpUtil.getTokenValue());
    }


}
