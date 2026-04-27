package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.mapper.GroupRoomMapper;
import com.abc.chat4j.platform.service.GroupRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupRoomServiceImpl extends ServiceImpl<GroupRoomMapper, GroupRoom> implements GroupRoomService {

    @Autowired
    private GroupRoomMapper groupRoomMapper;

    @Override
    public GroupMember selectGroupMemberByRoomIdAndUid(Long roomId, Long userId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        return groupRoomMapper.selectByRoomIdAndUid(roomId, userId);
    }
}
