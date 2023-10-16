package com.shiyi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.SiteClass;
import com.shiyi.mapper.SiteClassMapper;
import com.shiyi.service.SiteClassService;
import com.shiyi.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Service
public class SiteClassServiceImpl extends ServiceImpl<SiteClassMapper, SiteClass> implements SiteClassService {


    @Override
    public ResponseResult selectSiteClassList() {
        Page<SiteClass> siteClassPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), null);
        return ResponseResult.success(siteClassPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSiteClass(SiteClass siteClass) {
        baseMapper.insert(siteClass);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSiteClass(SiteClass siteClass) {
        baseMapper.updateById(siteClass);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }
}
