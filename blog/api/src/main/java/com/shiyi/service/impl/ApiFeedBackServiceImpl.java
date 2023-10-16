package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FeedBack;
import com.shiyi.mapper.FeedBackMapper;
import com.shiyi.service.ApiFeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiFeedBackServiceImpl implements ApiFeedBackService {

    private final FeedBackMapper feedBackMapper;

    /**
     * 添加反馈
     *
     * @param feedBack
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertFeedback(FeedBack feedBack) {
        feedBack.setUserId(StpUtil.getLoginIdAsString());
        int rows = feedBackMapper.insert(feedBack);
        return rows > 0 ? ResponseResult.success() : ResponseResult.error("添加反馈失败");
    }
}
