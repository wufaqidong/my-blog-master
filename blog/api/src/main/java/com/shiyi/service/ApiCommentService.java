package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Comment;

public interface ApiCommentService {

    /**
     * 发表评论
     * @param comment
     * @return
     */
    public ResponseResult insertComment(Comment comment);

    /**
     * 分页获取文章评论
     * @param pageNo
     * @param pageSize
     * @param articleId
     * @return
     */
    public ResponseResult selectCommentByArticleId(int pageNo, int pageSize, Long articleId);

    /**
     * 获取我的评论
     * @return
     */
    public ResponseResult selectMyComment();


}
