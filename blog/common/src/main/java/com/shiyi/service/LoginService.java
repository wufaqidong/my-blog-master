package com.shiyi.service;


import com.shiyi.common.ResponseResult;
import com.shiyi.dto.Captcha;
import com.shiyi.dto.LoginDTO;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:58
 */
public interface LoginService {

    ResponseResult login(LoginDTO vo);

    ResponseResult getCaptcha(Captcha captcha);
}
