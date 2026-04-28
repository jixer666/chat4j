package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.mapper.GroupMemberMapper;
import com.abc.chat4j.platform.service.GroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Override
    public GroupMember selectGroupMemberByRoomIdAndUid(Long roomId, Long userId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        return groupMemberMapper.selectByRoomIdAndUid(roomId, userId);
    }

    @Override
    public List<GroupMember> selectGroupMemberByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return groupMemberMapper.selectGroupMemberByRoomId(roomId);
    }
}
