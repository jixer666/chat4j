package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.platform.domain.context.GroupMemberCreateContext;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.mapper.GroupMemberMapper;
import com.abc.chat4j.platform.service.GroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public void saveGroupMemberBatch(GroupMemberCreateContext context) {
        if (CollectionUtil.isEmpty(context.getUserIdList())) {
            return;
        }

        // 获取已经保存群聊成员
        List<GroupMember> groupMemberList = groupMemberMapper.selectGroupMemberByRoomIdAndUserIdList(context.getRoomId(), context.getUserIdList());
        Map<Object, GroupMember> existGroupMemberMap = groupMemberList.stream().collect(Collectors.toMap(new Function<GroupMember, Object>() {
            @Override
            public Object apply(GroupMember groupMember) {
                return groupMember.getGroupId() + "_" + groupMember.getUserId();
            }
        }, Function.identity()));
        // 筛选需要保存的群聊成员
        List<GroupMember> saveGroupMemberList = context.getUserIdList().stream()
                .filter(item -> !existGroupMemberMap.containsKey(context.getRoomId() + "_" + item))
                .map(item -> {
                    GroupMember groupMember = new GroupMember();
                    groupMember.setUserId(item);
                    groupMember.setGroupMemberId(IdUtils.getId());
                    groupMember.setRoomId(context.getRoomId());
                    groupMember.setGroupId(context.getGroupRoomId());
                    groupMember.setCommonParams();
                    return groupMember;
                }).collect(Collectors.toList());

        groupMemberMapper.insertGroupMemberBatch(saveGroupMemberList);
    }
}
