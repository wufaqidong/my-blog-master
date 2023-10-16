package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Emoji;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.EmojiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiEmojiServiceImpl implements com.shiyi.service.ApiEmojiService {

    private final EmojiMapper emojiMapper;

    @Override
    public ResponseResult selectEmojiListByUserId() {
        List<Emoji> emojis = emojiMapper.selectList(new LambdaQueryWrapper<Emoji>()
                .eq(Emoji::getUserId, StpUtil.getLoginIdAsString()).orderByDesc(Emoji::getIsStick).orderByDesc(Emoji::getCreateTime));
        return ResponseResult.success(emojis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addEmoji(Emoji emoji) {
        int count = emojiMapper.selectCount(new LambdaQueryWrapper<Emoji>().eq(Emoji::getUserId, StpUtil.getLoginIdAsString())
                .eq(Emoji::getUrl, emoji.getUrl()));
        if (count > 0) {
            throw new BusinessException("该表情已存在");
        }
        emoji.setUserId(StpUtil.getLoginIdAsString());
        emojiMapper.insert(emoji);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteEmoji(Integer id) {
        Emoji emoji = emojiMapper.selectById(id);
        if (!emoji.getUserId().equals(StpUtil.getLoginIdAsString())){
            throw new BusinessException("删除失败，非本人创建的emoji不能删除");
        }
        emojiMapper.deleteById(id);
        return ResponseResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult stickEmoji(Integer id) {
        Emoji emoji = emojiMapper.selectById(id);
        if (!emoji.getUserId().equals(StpUtil.getLoginIdAsString())){
            throw new BusinessException("置顶失败，非本人创建的emoji不能置顶");
        }
        emoji.setIsStick(1);
        emojiMapper.updateById(emoji);
        return ResponseResult.success();
    }
}
