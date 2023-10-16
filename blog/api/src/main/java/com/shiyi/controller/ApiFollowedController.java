package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.AccessLimit;
import com.shiyi.common.ResponseResult;
import com.shiyi.service.ApiFollowedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/followed")
@RequiredArgsConstructor
@Api(tags = "关注API-V1")
public class ApiFollowedController {

    private final ApiFollowedService followedService;

    @AccessLimit
    @SaCheckLogin
    @PostMapping(value = "/insertFollowed")
    @ApiOperation(value = "关注用户", httpMethod = "POST", response = ResponseResult.class, notes = "关注用户")
    public ResponseResult insertFeedback(String userId) {
        return  followedService.insertFollowed(userId);
    }

    @AccessLimit
    @SaCheckLogin
    @DeleteMapping(value = "/deleteFollowed")
    @ApiOperation(value = "取消关注用户", httpMethod = "DELETE", response = ResponseResult.class, notes = "取消关注用户")
    public ResponseResult deleteFollowed(String userId) {
        return  followedService.deleteFollowed(userId);
    }
}
