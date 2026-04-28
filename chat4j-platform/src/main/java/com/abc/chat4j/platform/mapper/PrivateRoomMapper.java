package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrivateRoomMapper extends BaseMapper<PrivateRoom> {
    PrivateRoom selectByRoomId(Long roomId);

    List<PrivateRoom> selectPrivateRoomListByRoomIds(@Param("roomIds") List<Long> roomIds);
}
