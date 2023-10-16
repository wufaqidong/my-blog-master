package com.shiyi.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.SiteClass;
import com.shiyi.service.FeedBackService;
import com.shiyi.service.SiteClassService;
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
@RequestMapping("/system/siteClass")
@Api(tags = "后台网址分类管理")
@RequiredArgsConstructor
public class SiteClassController {

    private final SiteClassService siteClassService;

    @SaCheckLogin
    @GetMapping(value = "/list")
    @ApiOperation(value = "网址分类列表", httpMethod = "GET", response = ResponseResult.class, notes = "网址分类列表")
    public ResponseResult selectSiteClassList() {
        return siteClassService.selectSiteClassList();
    }

    @PostMapping(value = "/insert")
    @SaCheckPermission("/system/siteClass/insert")
    @ApiOperation(value = "添加网址分类", httpMethod = "POST", response = ResponseResult.class, notes = "添加网址分类")
    @OperationLogger(value = "添加网址分类")
    public ResponseResult insert(@RequestBody SiteClass siteClass) {
        return siteClassService.insertSiteClass(siteClass);
    }

    @PutMapping(value = "/update")
    @SaCheckPermission("/system/siteClass/update")
    @ApiOperation(value = "修改网址分类", httpMethod = "PUT", response = ResponseResult.class, notes = "修改网址分类")
    @OperationLogger(value = "修改网址分类")
    public ResponseResult update(@RequestBody SiteClass siteClass) {
        return siteClassService.updateSiteClass(siteClass);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/siteClass/deleteBatch")
    @ApiOperation(value = "删除网址分类", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除网址分类")
    @OperationLogger(value = "删除网址分类")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return siteClassService.deleteBatch(ids);
    }
}

