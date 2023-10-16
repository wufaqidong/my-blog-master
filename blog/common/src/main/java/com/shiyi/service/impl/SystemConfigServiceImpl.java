package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.SystemConfig;
import com.shiyi.mapper.SystemConfigMapper;
import com.shiyi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shiyi.common.FieldConstants.LIMIT_ONE;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {


    /**
     * 获取系统配置
     * @return
     */
    @Override
    public ResponseResult getConfig() {
        SystemConfig systemConfig = baseMapper.selectOne(new QueryWrapper<SystemConfig>().last(LIMIT_ONE));
        if (!StpUtil.hasRole("admin")){
            systemConfig.setQiNiuAccessKey(null);
            systemConfig.setQiNiuSecretKey(null);
            systemConfig.setEmailPassword(null);
            systemConfig.setEmailUsername(null);
        }
        return ResponseResult.success(systemConfig);
    }

    /**
     * 修改系统配置
     * @param systemConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
        return ResponseResult.success();
    }

    //---------自定义方法----------
    @Override
    public SystemConfig getCustomizeOne() {
        return baseMapper.selectOne(new QueryWrapper<SystemConfig>().last(LIMIT_ONE));
    }
}
