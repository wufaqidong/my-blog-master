package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    void passBatch(@Param("ids") List<Integer> ids);
}
