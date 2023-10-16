package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Say;
import com.shiyi.mapper.SayMapper;
import com.shiyi.service.SayService;
import com.shiyi.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SayServiceImpl extends ServiceImpl<SayMapper, Say> implements SayService {


    @Override
    public ResponseResult selectSayList(String keywords) {
        LambdaQueryWrapper<Say> sayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StpUtil.hasRole("admin")) {
            sayLambdaQueryWrapper.eq(Say::getIsPublic,1);
        }
        sayLambdaQueryWrapper.orderByDesc(Say::getCreateTime);
        Page<Say> sayPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),sayLambdaQueryWrapper);
        return ResponseResult.success(sayPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSay(Say say) {
        say.setUserId(StpUtil.getLoginIdAsString());
        baseMapper.insert(say);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteSay(List<String> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult selectSayById(String id) {
        Say say = baseMapper.selectById(id);
        return ResponseResult.success(say);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSayById(Say say) {
        baseMapper.updateById(say);
        return ResponseResult.success();
    }
}
