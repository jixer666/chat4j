package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivateRoomMapper extends BaseMapper<PrivateRoom> {
    PrivateRoom selectByRoomId(Long roomId);
}
