package com.shiyi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Comment;
import com.shiyi.mapper.CommentMapper;
import com.shiyi.service.CommentService;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.SystemCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    /**
     * 评论列表
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listComment(String keywords) {
        Page<SystemCommentVO> dtoPage = baseMapper.selectPageList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), keywords);
        return ResponseResult.success(dtoPage);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

}
