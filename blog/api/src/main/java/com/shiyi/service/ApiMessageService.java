package com.shiyi.service;


import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Message;

public interface ApiMessageService {

    /**
     * 获取所有留言
     * @return
     */
    ResponseResult selectMessageList();

    /**
     * 添加留言
     * @param message
     * @return
     */
    ResponseResult insertMessage(Message message);
}
