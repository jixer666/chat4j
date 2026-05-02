package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.platform.domain.context.RoomCreateContext;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.mapper.GroupRoomMapper;
import com.abc.chat4j.platform.service.GroupRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GroupRoomServiceImpl extends ServiceImpl<GroupRoomMapper, GroupRoom> implements GroupRoomService {

    @Autowired
    private GroupRoomMapper groupRoomMapper;

    @Override
    public List<GroupRoom> selectGroupRoomListByRoomIds(List<Long> roomIds) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return groupRoomMapper.selectGroupRoomListByRoomIds(roomIds);
    }

    @Override
    public GroupRoom createGroupRoom(RoomCreateContext context) {
        checkCreateGroupRoomParams(context);

        GroupRoom groupRoom = new GroupRoom();
        groupRoom.setGroupRoomId(IdUtils.getId());
        groupRoom.setRoomId(context.getRoomId());
        groupRoom.setUserId(context.getUserId());
        groupRoom.setName(CommonConstants.DEFAULT_GROUP_NAME);
        groupRoom.setAvatar(CommonConstants.DEFAULT_GROUP_AVATAR);

        groupRoomMapper.insert(groupRoom);

        return groupRoom;
    }

    private void checkCreateGroupRoomParams(RoomCreateContext context) {
        AssertUtils.isNotEmpty(context, "参数不能为空");
        AssertUtils.isNotEmpty(context.getRoomId(), "房间ID不能为空");
        AssertUtils.isNotEmpty(context.getUserId(), "用户ID不能为空");
    }
}
