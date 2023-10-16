package com.shiyi.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Navigation;
import com.shiyi.service.NavigationService;
import com.shiyi.service.NavigationService;
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
@RequestMapping("/system/navigation")
@Api(tags = "后台网址导航管理")
@RequiredArgsConstructor
public class NavigationController {

    private final NavigationService navigationService;

    @SaCheckLogin
    @GetMapping(value = "/list")
    @ApiOperation(value = "网址导航列表", httpMethod = "GET", response = ResponseResult.class, notes = "网址导航列表")
    public ResponseResult selectNavigationList(Integer siteClassId) {
        return navigationService.selectNavigationList(siteClassId);
    }

    @PostMapping(value = "/insert")
    @SaCheckPermission("/system/Navigation/insert")
    @ApiOperation(value = "添加网址导航", httpMethod = "POST", response = ResponseResult.class, notes = "添加网址导航")
    @OperationLogger(value = "添加网址导航")
    public ResponseResult insert(@RequestBody Navigation Navigation) {
        return navigationService.insertNavigation(Navigation);
    }
    
    @PutMapping(value = "/update")
    @SaCheckPermission("/system/Navigation/update")
    @ApiOperation(value = "修改网址导航", httpMethod = "PUT", response = ResponseResult.class, notes = "修改网址导航")
    @OperationLogger(value = "修改网址导航")
    public ResponseResult update(@RequestBody Navigation Navigation) {
        return navigationService.updateNavigation(Navigation);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/Navigation/deleteBatch")
    @ApiOperation(value = "删除网址导航", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除网址导航")
    @OperationLogger(value = "删除网址导航")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return navigationService.deleteBatch(ids);
    }
}

