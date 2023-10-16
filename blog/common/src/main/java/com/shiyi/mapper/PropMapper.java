package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.entity.Prop;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface PropMapper extends BaseMapper<Prop> {


    /**
     * 根据用户id和道具id查看用户获取的详情
     * @param userId 用户id
     * @param id 道具id
     * @return
     */
    Integer selectPropByUserIdAndPropId(@Param("userId") String userId, @Param("propId")Integer id);

    /**
     * 扣除道具卡
     * @param userId 用户id
     * @param id 道具id
     * @param num 数据
     */
    void deductPropByUserIdAndPropId(@Param("userId") String userId, @Param("propId")Integer id,@Param("num")int num);
}
