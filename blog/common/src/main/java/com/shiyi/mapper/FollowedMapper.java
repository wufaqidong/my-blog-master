package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.entity.Followed;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface FollowedMapper extends BaseMapper<Followed> {

    /**
     * 统计当前用户七天的关注量
     * @param id
     * @return
     */
    int countQiDay(String id);
}
