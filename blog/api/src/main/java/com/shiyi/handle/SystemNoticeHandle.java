package com.shiyi.handle;

import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.entity.ImMessage;
import com.shiyi.mapper.ImMessageMapper;
import com.shiyi.utils.IpUtil;
import com.shiyi.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SystemNoticeHandle {


    /**
     *
     * 发送系统通知
     * @param toUserId
     * @param noticeType
     * @param noticeCode
     */
    public static void sendNotice(String toUserId,Integer noticeType,Integer noticeCode,Integer articleId,Integer commentMark,String content) {
        ImMessageMapper imMessageMapper = SpringUtils.getBean(ImMessageMapper.class);
        try {
            String ip = IpUtil.getIp();

            ImMessage message = ImMessage.builder().fromUserId(StpUtil.getLoginIdAsString()).toUserId(toUserId).content(content).commentMark(commentMark)
                    .noticeType(noticeType).code(noticeCode).ip(ip).ipSource(IpUtil.getIp2region(ip)).articleId(articleId).build();
            imMessageMapper.insert(message);
        } catch (Exception e) {
            //添加失败的话不抛异常，还是要执行成功
            log.error("生成消息通知失败，错误原因：{}",e.getMessage());
        }
    }
}
