package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.entity.Comment;
import com.shiyi.vo.ApiArticleListVO;
import com.shiyi.vo.ApiCommentListVO;
import com.shiyi.vo.SystemCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 博客评论表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {


    Page<SystemCommentVO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);

    Page<ApiCommentListVO> selectCommentPage(@Param("page") Page<ApiCommentListVO> commentListVOPage,
                                             @Param("articleId") Long articleId);

    /**
     * 获取我的文章
     * @param apiArticleListVOPage
     * @param loginIdAsString 登录用户id
     * @return
     */
    Page<ApiArticleListVO> selectMyComment(Page<ApiArticleListVO> apiArticleListVOPage, @Param("userId") String loginIdAsString);
}
