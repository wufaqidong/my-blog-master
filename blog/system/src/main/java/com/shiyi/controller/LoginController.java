package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.common.ResponseResult;
import com.shiyi.dto.Captcha;
import com.shiyi.dto.LoginDTO;
import com.shiyi.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:37
 */
@RestController
@Api(tags = "登录-接口")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @ApiOperation(value = "生成验证码拼图")
    @PostMapping("get-captcha")
    public ResponseResult getCaptcha(@RequestBody Captcha captcha) {
        return ResponseResult.success(loginService.getCaptcha(captcha));
    }

    @PostMapping("login")
    public ResponseResult login(@Validated @RequestBody LoginDTO vo) {
        return loginService.login(vo);
    }

    @SaCheckLogin
    @GetMapping("logout")
    public ResponseResult logout() {
        StpUtil.logout();
        return ResponseResult.success("退出成功");
    }
}
