package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupRoomMapper extends BaseMapper<GroupRoom> {
    List<GroupRoom> selectGroupRoomListByRoomIds(@Param("roomIds") List<Long> roomIds);
}
