package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Message;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
public interface MessageService extends IService<Message> {

    ResponseResult listMessage(String name);

    ResponseResult deleteMessageById(int id);

    ResponseResult passBatch(List<Integer> ids);

    ResponseResult deleteBatch(List<Integer> ids);





}
