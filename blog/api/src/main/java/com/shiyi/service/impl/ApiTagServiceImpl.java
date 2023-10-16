package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Tags;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.service.ApiTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {

    private final TagsMapper tagsMapper;

    /**
     *  标签列表
     * @return
     */
    @Override
    public ResponseResult selectTagList() {
        List<Tags> tags = tagsMapper.selectList(new LambdaQueryWrapper<Tags>().orderByDesc(Tags::getSort));
        return ResponseResult.success(tags);
    }
}
