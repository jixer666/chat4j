package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.domain.enums.StatusEnum;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.cache.GroupRoomCache;
import com.abc.chat4j.platform.cache.PrivateRoomCache;
import com.abc.chat4j.platform.cache.RoomCache;
import com.abc.chat4j.platform.domain.entity.*;
import com.abc.chat4j.platform.domain.enums.RoomTypeEnum;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.abc.chat4j.platform.mapper.RoomMapper;
import com.abc.chat4j.platform.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private PrivateRoomService privateRoomService;

    @Autowired
    private GroupRoomService groupRoomService;

    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    private RoomCache roomCache;

    @Autowired
    private PrivateRoomCache privateRoomCache;

    @Autowired
    private GroupRoomCache groupRoomCache;

    @Autowired
    private GroupMemberService groupMemberService;

    @Override
    public Room selectRoomByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return roomMapper.selectById(roomId);
    }

    @Override
    public List<Room> selectRoomListByRoomIds(List<Long> roomIds) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return roomMapper.selectBatchIds(roomIds);
    }

    @Override
    public void checkUserInRoom(Long userId, Long roomId) {
        Room room = selectRoomByRoomId(roomId);
        AssertUtils.isNotEmpty(room, "未知房间");
        if (RoomTypeEnum.PRIVATE.getType().equals(room.getType())) {
            PrivateRoom privateRoom = privateRoomService.selectPrivateRoomByRoomId(roomId);
            // 判断私聊房间用户是否在双方访问内
            Long uid1 = privateRoom.getUserId(), uid2 = privateRoom.getFriendId();
            AssertUtils.isTrue(uid1.equals(userId) || uid2.equals(roomId), "不在房间，无法聊天");
            // 判断有无私聊无好友关系
            UserFriend userFriend = userFriendService.selectUserFriendByUidAndFriendId(userId,
                    uid1.equals(userId) ? uid2 : uid1);
            AssertUtils.isTrue(Objects.isNull(userFriend) ||
                            !StatusEnum.NORMAL.getStatus().equals(userFriend.getStatus()), "不是好友关系，无法发送消息");
        } else {
            // 判断是否不属于群成员
            GroupMember groupMember = groupMemberService.selectGroupMemberByRoomIdAndUid(roomId, userId);
            AssertUtils.isNotEmpty(groupMember, "不是群成员，无法聊天");
        }
    }

    @Override
    public List<RoomInfoVO> selectRoomInfoVOListByRoomIds(List<Long> roomIdList) {
        if (CollectionUtil.isEmpty(roomIdList)) {
            return new ArrayList<>();
        }

        Map<Long, Room> roomMap = roomCache.getBatch(roomIdList);
        List<Long> privateRoomIdList = new ArrayList<>();
        List<Long> groupRoomIdList = new ArrayList<>();
        for (Room room : roomMap.values()) {
            if (RoomTypeEnum.PRIVATE.getType().equals(room.getType())) {
                privateRoomIdList.add(room.getRoomId());
            } else {
                groupRoomIdList.add(room.getRoomId());
            }
        }
        Map<Long, PrivateRoom> privateRoomMap = privateRoomCache.getBatch(privateRoomIdList);
        Map<Long, GroupRoom> groupRoomMap = groupRoomCache.getBatch(groupRoomIdList);

        return roomMap.values().stream().map(item -> {
            RoomInfoVO roomInfoVO = new RoomInfoVO();
            roomInfoVO.setRoomId(item.getRoomId());
            roomInfoVO.setType(item.getType());
            if (RoomTypeEnum.PRIVATE.getType().equals(item.getType())) {
                roomInfoVO.setData(privateRoomMap.get(item.getRoomId()));
            } else {
                roomInfoVO.setData(groupRoomMap.get(item.getRoomId()));
            }
            return roomInfoVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getRoomMemberListByRoomId(Long roomId) {
        if (Objects.isNull(roomId)) {
            return new ArrayList<>();
        }

        List<Long> userIdList = new ArrayList<>();
        Room room = roomCache.get(roomId);
        if (RoomTypeEnum.PRIVATE.getType().equals(room.getType())) {
            PrivateRoom privateRoom = privateRoomService.selectPrivateRoomByRoomId(roomId);
            userIdList.add(privateRoom.getUserId());
            userIdList.add(privateRoom.getFriendId());
        } else {
            List<GroupMember> groupMember = groupMemberService.selectGroupMemberByRoomId(roomId);
            userIdList.addAll(groupMember.stream().map(GroupMember::getUserId).collect(Collectors.toList()));
        }

        return userIdList;
    }
}
