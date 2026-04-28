package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    GroupMember selectByRoomIdAndUid(@Param("roomId") Long roomId,
                                      @Param("userId") Long userId);

    List<GroupMember> selectGroupMemberByRoomId(Long roomId);
}
