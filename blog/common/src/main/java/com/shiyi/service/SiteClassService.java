package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.SiteClass;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
public interface SiteClassService extends IService<SiteClass> {


    ResponseResult selectSiteClassList();


    ResponseResult insertSiteClass(SiteClass siteClass);

    ResponseResult updateSiteClass(SiteClass siteClass);


    ResponseResult deleteBatch(List<Integer> ids);

}
