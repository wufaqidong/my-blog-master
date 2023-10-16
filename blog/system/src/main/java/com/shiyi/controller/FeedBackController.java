package com.shiyi.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FeedBack;
import com.shiyi.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/system/feedback")
@Api(tags = "后台反馈管理")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "反馈列表", httpMethod = "GET", response = ResponseResult.class, notes = "反馈列表")
    public ResponseResult list(Integer type) {
        return feedBackService.listFeedBack(type);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/feedback/deleteBatch")
    @ApiOperation(value = "删除反馈", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除反馈")
    @OperationLogger(value = "删除反馈")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return feedBackService.deleteBatch(ids);
    }

    @PutMapping(value = "/update")
    @OperationLogger(value = "修改反馈")
    @SaCheckPermission("/system/feedback/update")
    @ApiOperation(value = "修改反馈", httpMethod = "PUT", response = ResponseResult.class, notes = "修改反馈")
    public ResponseResult update(@RequestBody FeedBack feedBack) {
        return feedBackService.updateFeedBack(feedBack);
    }
}

