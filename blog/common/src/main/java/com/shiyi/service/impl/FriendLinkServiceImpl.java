package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.FieldConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FriendLink;
import com.shiyi.mapper.FriendLinkMapper;
import com.shiyi.service.EmailService;
import com.shiyi.service.FriendLinkService;
import com.shiyi.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    private final EmailService emailService;

    /**
     * 友链列表
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult listFriendLink(String name, Integer status) {
        QueryWrapper<FriendLink> queryWrapper= new QueryWrapper<FriendLink>()
                .orderByDesc(FieldConstants.SORT).like(StringUtils.isNotBlank(name), FieldConstants.NAME,name)
                .eq(status != null, FieldConstants.STATUS,status);
        Page<FriendLink> friendLinkPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return ResponseResult.success(friendLinkPage);
    }

    /**
     * 添加友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertFriendLink(FriendLink friendLink) {
        baseMapper.insert(friendLink);
        return ResponseResult.success();
    }

    /**
     * 修改友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFriendLink(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        //审核通过和未通过发送邮件通知
        emailService.sendFriendEmail(friendLink);
        return ResponseResult.success();
    }

    /**
     * 删除友链
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("删除友链失败");
    }

    /**
     * 置顶友链
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id,sort+1);
        return ResponseResult.success();
    }


}
