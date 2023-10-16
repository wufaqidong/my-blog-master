package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Say;
import com.shiyi.service.CommentService;
import com.shiyi.service.SayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/say")
@Api(tags = "说说管理")
@RequiredArgsConstructor
public class SayController {

    private final SayService sayService;

    @SaCheckLogin
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "说说列表", httpMethod = "GET", response = ResponseResult.class, notes = "说说列表")
    public ResponseResult list(String keywords){
        return sayService.selectSayList(keywords);
    }

    @SaCheckLogin
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiOperation(value = "说说详情", httpMethod = "GET", response = ResponseResult.class, notes = "说说详情")
    public ResponseResult info(String id){
        return sayService.selectSayById(id);
    }

    @SaCheckLogin
    @OperationLogger(value = "修改说说")
    @SaCheckPermission("/system/say/update")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @ApiOperation(value = "修改说说", httpMethod = "PUT", response = ResponseResult.class, notes = "修改说说")
    public ResponseResult update(@RequestBody Say say){
        return sayService.updateSayById(say);
    }

    @OperationLogger(value = "发表说说")
    @SaCheckPermission("/system/say/")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ApiOperation(value = "发表说说", httpMethod = "POST", response = ResponseResult.class, notes = "发表说说")
    public ResponseResult insertSay(@RequestBody Say say){
        return sayService.insertSay(say);
    }

    @OperationLogger(value = "删除说说")
    @SaCheckPermission("/system/say/delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除说说", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除说说")
    public ResponseResult deleteSay(@RequestBody List<String> ids){
        return sayService.deleteSay(ids);
    }

}
