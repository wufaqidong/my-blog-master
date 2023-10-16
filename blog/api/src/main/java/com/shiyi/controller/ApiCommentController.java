package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.AccessLimit;
import com.shiyi.annotation.BusinessLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Comment;
import com.shiyi.service.ApiCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/comment")
@Api(tags = "评论API-V1")
@RequiredArgsConstructor
public class ApiCommentController {

    private final ApiCommentService commentService;

    @AccessLimit
    @SaCheckLogin
    @BusinessLogger(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ApiOperation(value = "添加评论", httpMethod = "POST", response = ResponseResult.class, notes = "添加评论")
    public ResponseResult insertComment(@RequestBody Comment comment){
        return commentService.insertComment(comment);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value = "根据文章id获取相关评论", httpMethod = "GET", response = ResponseResult.class, notes = "根据文章id获取相关评论")
    public ResponseResult selectCommentByArticleId(int pageNo,int pageSize,Long articleId){
        return commentService.selectCommentByArticleId(pageNo,pageSize,articleId);
    }

    @RequestMapping(value = "/getMyComment",method = RequestMethod.GET)
    @ApiOperation(value = "获取我的评论", httpMethod = "GET", response = ResponseResult.class, notes = "获取我的评论")
    public ResponseResult selectMyComment(){
        return commentService.selectMyComment();
    }
}
