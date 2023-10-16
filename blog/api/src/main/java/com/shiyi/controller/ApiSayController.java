package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Say;
import com.shiyi.entity.SayComment;
import com.shiyi.service.ApiSayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "说说接口-API")
@RequestMapping("v1/say")
@RequiredArgsConstructor
public class ApiSayController {

    private final ApiSayService apiSayService;

    @RequestMapping(value = "getSayList",method = RequestMethod.GET)
    @ApiOperation(value = "说说列表", httpMethod = "GET", response = ResponseResult.class, notes = "说说列表")
    public ResponseResult selectSayList(){
        return apiSayService.selectSayList();
    }

    @SaCheckLogin
    @RequestMapping(value = "like",method = RequestMethod.GET)
    @ApiOperation(value = "点赞说说", httpMethod = "GET", response = ResponseResult.class, notes = "点赞说说")
    public ResponseResult like(String sayId){
        return apiSayService.like(sayId);
    }

    @SaCheckLogin
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    @ApiOperation(value = "评论说说", httpMethod = "POST", response = ResponseResult.class, notes = "评论说说")
    public ResponseResult comment(@RequestBody SayComment sayComment){
        return apiSayService.comment(sayComment);
    }

    @SaCheckLogin
    @RequestMapping(value = "insertSay",method = RequestMethod.POST)
    @ApiOperation(value = "添加说说说", httpMethod = "POST", response = ResponseResult.class, notes = "添加说说说")
    public ResponseResult insertSay(@RequestBody Say say){
        return apiSayService.insertSay(say);
    }

}
