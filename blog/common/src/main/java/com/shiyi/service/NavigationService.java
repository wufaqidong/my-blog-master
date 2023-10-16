package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Navigation;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
public interface NavigationService extends IService<Navigation> {

    ResponseResult selectNavigationList(Integer siteClassId);

    ResponseResult insertNavigation(Navigation navigation);

    ResponseResult updateNavigation(Navigation navigation);

    ResponseResult deleteBatch(List<Integer> ids);
}
