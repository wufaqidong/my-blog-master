package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FeedBack;

public interface ApiFeedBackService {
    /**
     * 添加反馈
     * @return
     */
    public ResponseResult insertFeedback(FeedBack feedBack);


}
