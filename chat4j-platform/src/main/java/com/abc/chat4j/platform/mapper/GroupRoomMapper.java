package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupRoomMapper extends BaseMapper<GroupRoom> {
    GroupMember selectByRoomIdAndUid(@Param("roomId") Long roomId,
                                     @Param("userId") Long userId);
}
