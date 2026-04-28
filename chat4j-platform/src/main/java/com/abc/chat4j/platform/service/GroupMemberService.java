package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GroupMemberService extends IService<GroupMember> {
    GroupMember selectGroupMemberByRoomIdAndUid(Long roomId, Long userId);

    List<GroupMember> selectGroupMemberByRoomId(Long roomId);
}
