package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Article;
import com.shiyi.entity.Collect;
import com.shiyi.entity.Tags;
import com.shiyi.handle.SystemNoticeHandle;
import com.shiyi.im.MessageConstant;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.CollectMapper;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.service.ApiCollectService;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.ApiArticleListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCollectServiceImpl implements ApiCollectService {

    private final CollectMapper collectMapper;

    private final ArticleMapper articleMapper;

    private final TagsMapper tagsMapper;

    /**
     * 我的收藏列表
     * @return
     */
    @Override
    public ResponseResult selectCollectList() {
        Page<ApiArticleListVO> list = collectMapper.selectCollectList(new Page<ApiArticleListVO>(PageUtils.getPageNo(),PageUtils.getPageSize()),StpUtil.getLoginIdAsString());
        list.getRecords().forEach(item ->{
            List<Tags> tags = tagsMapper.selectTagByArticleId(item.getId());
            item.setTagList(tags);
        });
        return ResponseResult.success(list);
    }

    /**
     * 收藏文章
     * @param articleId 文章id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult collect(Integer articleId) {
        Collect collect = Collect.builder().userId(StpUtil.getLoginIdAsString()).articleId(articleId).build();
        collectMapper.insert(collect);
        // 发送系统通知
        Article article = articleMapper.selectById(articleId);
        SystemNoticeHandle.sendNotice(article.getUserId(),MessageConstant.MESSAGE_COLLECT_NOTICE,MessageConstant.SYSTEM_MESSAGE_CODE,articleId,null,null);
        return ResponseResult.success();
    }

    /**
     * 取消收藏
     * @param articleId 文章id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult cancel(Integer articleId) {
        collectMapper.delete(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId,StpUtil.getLoginIdAsString()).eq(Collect::getArticleId,articleId));
        return ResponseResult.success();
    }
}
