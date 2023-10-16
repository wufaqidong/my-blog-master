package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.AccessLimit;
import com.shiyi.common.ResponseResult;
import com.shiyi.service.ApiCollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author blue
 * @date 2022/1/13
 * @apiNote
 */
@RestController
@RequestMapping("/v1/collect")
@RequiredArgsConstructor
@Api(tags = "文章收藏API-V1")
public class ApiCollectController {

    private final ApiCollectService apiCollectService;

    @SaCheckLogin
    @GetMapping(value = "/")
    @ApiOperation(value = "我的收藏列表", httpMethod = "GET", response = ResponseResult.class, notes = "我的收藏列表")
    public ResponseResult selectCollectList() {
        return apiCollectService.selectCollectList();
    }

    @SaCheckLogin
    @AccessLimit
    @GetMapping(value = "collect")
    @ApiOperation(value = "收藏文章", httpMethod = "GET", response = ResponseResult.class, notes = "收藏文章")
    public ResponseResult collect(Integer articleId) {
        return apiCollectService.collect(articleId);
    }

    @SaCheckLogin
    @AccessLimit
    @DeleteMapping(value = "/")
    @ApiOperation(value = "取消收藏", httpMethod = "DELETE", response = ResponseResult.class, notes = "取消收藏")
    public ResponseResult cancel(Integer articleId) {
        return apiCollectService.cancel(articleId);
    }
}
