package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.entity.ImRoom;
import com.shiyi.vo.ImRoomListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface ImRoomMapper extends BaseMapper<ImRoom> {


    List<ImRoomListVO> selectListByUserId(String userId);
}
