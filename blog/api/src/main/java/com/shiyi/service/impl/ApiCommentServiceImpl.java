package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Article;
import com.shiyi.entity.Comment;
import com.shiyi.entity.ImMessage;
import com.shiyi.entity.UserInfo;
import com.shiyi.exception.BusinessException;
import com.shiyi.handle.RelativeDateFormat;
import com.shiyi.handle.SystemNoticeHandle;
import com.shiyi.im.MessageConstant;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.CommentMapper;
import com.shiyi.mapper.ImMessageMapper;
import com.shiyi.mapper.UserInfoMapper;
import com.shiyi.service.ApiCommentService;
import com.shiyi.utils.HTMLUtils;
import com.shiyi.utils.IpUtil;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.ApiArticleListVO;
import com.shiyi.vo.ApiCommentListVO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommentServiceImpl implements ApiCommentService {

    private final CommentMapper commentMapper;

    private final UserInfoMapper userInfoMapper;

    private final ArticleMapper articleMapper;

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertComment(Comment comment) {
        UserAgent userAgent = UserAgent.parseUserAgentString(IpUtil.getRequest().getHeader("user-agent"));
        //获取ip地址
        String ip = IpUtil.getIp();
        String ipAddress = IpUtil.getIp2region(ip);
        String os = userAgent.getOperatingSystem().getName();
        if (os.contains("mac") || os.contains("Mac")) {
            comment.setSystem("mac");
        } else if (os.contains("Windows")) {
            comment.setSystem("windowns");
        }else {
            comment.setSystem("android");
        }
        //过滤内容 如删除html标签和敏感词替换
        String content = HTMLUtils.deleteTag(comment.getContent());
        comment.setContent(content);
        comment.setSystemVersion(os);
        comment.setIpAddress(ipAddress);
        comment.setUserId(StpUtil.getLoginIdAsString());
        int insert = commentMapper.insert(comment);
        if (insert == 0){
            throw new BusinessException("评论失败");
        }
        String toUserId =  comment.getReplyUserId();
        int mark = toUserId == null ? 2 : 1;
        if (toUserId == null) {
            Article article = articleMapper.selectById(comment.getArticleId());
            toUserId =  article.getUserId();
        }
        SystemNoticeHandle.sendNotice(toUserId,MessageConstant.MESSAGE_COMMENT_NOTICE,MessageConstant.SYSTEM_MESSAGE_CODE,comment.getArticleId(),mark,comment.getContent());
        return ResponseResult.success(comment);
    }

    @Override
    public ResponseResult selectCommentByArticleId(int pageNo, int pageSize, Long articleId) {
        //获取评论父级评论
        Page<ApiCommentListVO> pageList = commentMapper.selectCommentPage(new Page<ApiCommentListVO>(pageNo,pageSize),articleId);
        //获取子级
        for (ApiCommentListVO vo : pageList.getRecords()) {
            List<Comment> commentList = commentMapper.selectList(
                    new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, vo.getId()).orderByDesc(Comment::getCreateTime));
            for (Comment e : commentList) {
                UserInfo replyUserInfo = userInfoMapper.getByUserId(e.getReplyUserId());
                UserInfo userInfo1 = userInfoMapper.getByUserId(e.getUserId());
                ApiCommentListVO apiCommentListVO = ApiCommentListVO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .replyUserId(e.getReplyUserId())
                        .nickname(userInfo1.getNickname())
                        .webSite(userInfo1.getWebSite())
                        .replyNickname(replyUserInfo.getNickname())
                        .replyWebSite(replyUserInfo.getWebSite())
                        .content(e.getContent())
                        .avatar(userInfo1.getAvatar())
                        .createTimeStr(RelativeDateFormat.format(e.getCreateTime()))
                        .browser(e.getBrowser())
                        .browserVersion(e.getBrowserVersion())
                        .system(e.getSystem())
                        .systemVersion(e.getSystemVersion())
                        .ipAddress(e.getIpAddress())
                        .build();
                vo.getChildren().add(apiCommentListVO);
            }
            vo.setCreateTimeStr(RelativeDateFormat.format(vo.getCreateTime()));
        }
        return ResponseResult.success(pageList);
    }

    /**
     * 获取我的评论
     * @return
     */
    @Override
    public ResponseResult selectMyComment() {
        Page<ApiArticleListVO> result  = commentMapper.selectMyComment(new Page<ApiArticleListVO>(PageUtils.getPageNo(),PageUtils.getPageSize()),StpUtil.getLoginIdAsString());
        return ResponseResult.success(result);
    }
}
