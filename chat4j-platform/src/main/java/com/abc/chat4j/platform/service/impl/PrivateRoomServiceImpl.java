package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.platform.domain.context.RoomCreateContext;
import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.mapper.PrivateRoomMapper;
import com.abc.chat4j.platform.service.PrivateRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PrivateRoomServiceImpl extends ServiceImpl<PrivateRoomMapper, PrivateRoom> implements PrivateRoomService {

    @Autowired
    private PrivateRoomMapper privateRoomMapper;

    @Override
    public PrivateRoom selectPrivateRoomByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return privateRoomMapper.selectByRoomId(roomId);
    }

    @Override
    public List<PrivateRoom> selectPrivateRoomListByRoomIds(List<Long> roomIds) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return privateRoomMapper.selectPrivateRoomListByRoomIds(roomIds);
    }

    @Override
    public PrivateRoom createPrivateRoom(RoomCreateContext context) {
        checkCreatePrivateRoomParams(context);

        String roomKey = getRoomKey(context.getUid1(), context.getUid2());
        PrivateRoom privateRoom = privateRoomMapper.selectByRoomKey(roomKey);
        if (Objects.nonNull(privateRoom)) {
            context.setRoomId(privateRoom.getRoomId());
            return privateRoom;
        }
        privateRoom = new  PrivateRoom();
        privateRoom.setPrivateRoomId(IdUtils.getId());
        privateRoom.setUserId(context.getUid1());
        privateRoom.setFriendId(context.getUid2());
        privateRoom.setRoomId(context.getRoomId());
        privateRoom.setRoomKey(roomKey);

        privateRoomMapper.insert(privateRoom);

        return privateRoom;
    }

    private void checkCreatePrivateRoomParams(RoomCreateContext context) {
        AssertUtils.isNotEmpty(context, "创建私聊房间参数不能为空");
        AssertUtils.isNotEmpty(context.getRoomId(), "房间ID不能为空");
        AssertUtils.isNotEmpty(context.getUid1(), "用户ID不能为空");
        AssertUtils.isNotEmpty(context.getUid2(), "用户ID不能为空");
    }

    private String getRoomKey(Long uid1, Long uid2) {
        return uid1 > uid2 ? uid2 + "_" + uid1 : uid1 + "_" + uid2;
    }
}
