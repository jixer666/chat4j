package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.baomidou.mybatisplus.extension.service.IService;

public interface GroupRoomService extends IService<GroupRoom> {
    GroupMember selectGroupMemberByRoomIdAndUid(Long roomId, Long userId);
}
