package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.domain.enums.StatusEnum;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.domain.entity.Room;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.enums.RoomTypeEnum;
import com.abc.chat4j.platform.mapper.RoomMapper;
import com.abc.chat4j.platform.service.GroupRoomService;
import com.abc.chat4j.platform.service.PrivateRoomService;
import com.abc.chat4j.platform.service.RoomService;
import com.abc.chat4j.platform.service.UserFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    @Override
    public Room selectRoomByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return roomMapper.selectById(roomId);
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
            GroupMember groupMember = groupRoomService.selectGroupMemberByRoomIdAndUid(roomId, userId);
            AssertUtils.isNotEmpty(groupMember, "不是群成员，无法聊天");
        }
    }
}
