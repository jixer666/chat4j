package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.domain.enums.StatusEnum;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.platform.cache.GroupRoomCache;
import com.abc.chat4j.platform.cache.PrivateRoomCache;
import com.abc.chat4j.platform.cache.RoomCache;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.ConversationCreateContext;
import com.abc.chat4j.platform.domain.context.RoomCreateContext;
import com.abc.chat4j.platform.domain.dto.RoomDTO;
import com.abc.chat4j.platform.domain.dto.StartGroupChatDTO;
import com.abc.chat4j.platform.domain.entity.*;
import com.abc.chat4j.platform.domain.enums.RoomTypeEnum;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.abc.chat4j.platform.mapper.RoomMapper;
import com.abc.chat4j.platform.service.*;
import com.abc.chat4j.system.cache.UserCache;
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

    @Autowired
    private UserCache userCache;

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
            AssertUtils.isTrue(uid1.equals(userId) || uid2.equals(userId), "不在房间，无法聊天");
            // 判断有无私聊无好友关系
            UserFriend userFriend = userFriendService.selectUserFriendByUidAndFriendId(userId,
                    uid1.equals(userId) ? uid2 : uid1);
            AssertUtils.isTrue(Objects.nonNull(userFriend) &&
                            StatusEnum.NORMAL.getStatus().equals(userFriend.getStatus()), "不是好友关系，无法发送消息");
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
    public List<Long> selectRoomMemberIdListByRoomId(Long roomId) {
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

    @Override
    public RoomInfoVO createRoom(RoomCreateContext context) {
        checkCreateRoomParams(context);

        RoomInfoVO roomInfoVO = new RoomInfoVO();
        roomInfoVO.setType(context.getType());
        Room room = buildRoomByType(context.getType());
        roomInfoVO.setRoomId(room.getRoomId());
        context.setRoomId(room.getRoomId());

        if (RoomTypeEnum.PRIVATE.getType().equals(context.getType())) {
            PrivateRoom privateRoom = privateRoomService.createPrivateRoom(context);
            roomInfoVO.setData(privateRoom);
        } else if (RoomTypeEnum.GROUP.getType().equals(context.getType())) {
            GroupRoom groupRoom = groupRoomService.createGroupRoom(context);
            roomInfoVO.setData(groupRoom);
        } else {
            throw new GlobalException("未知房间类型");
        }

        // 若不相等说明已经保存过房间了，无需保存
        // 此处只会在私聊流程中进行校验
        if (room.getRoomId().equals(context.getRoomId())) {
            roomMapper.insert(room);
        } else {
            roomInfoVO.setRoomId(context.getRoomId());
        }

        return roomInfoVO;
    }

    private Room buildRoomByType(Integer type) {
        Room room = new Room();
        room.setType(type);
        room.setRoomId(IdUtils.getId());
        room.setCommonParams();

        return room;
    }

    private void checkCreateRoomParams(RoomCreateContext context) {
        AssertUtils.isNotEmpty(context, "创建房间参数不能为空");
        AssertUtils.isNotEmpty(context.getType(), "房间类型不能为空");
    }

    @Override
    public RoomInfoVO startGroupChat(StartGroupChatDTO startGroupChatDTO) {
        checkStartGroupChatParams(startGroupChatDTO);

        // 创建群聊房间
        RoomCreateContext context = new RoomCreateContext();
        context.setType(RoomTypeEnum.GROUP.getType());
        context.setUserId(startGroupChatDTO.getUserId());
        context.setUserIdList(startGroupChatDTO.getUserIdList());
        RoomInfoVO roomInfoVO = createRoom(context);
        // 创建会话
        ConversationCreateContext conversationContext = new ConversationCreateContext();
        conversationContext.setRoomId(roomInfoVO.getRoomId());
        conversationContext.setUserIdList(startGroupChatDTO.getUserIdList());
        SpringUtil.getBean(ConversationService.class).createConversation(conversationContext);
        // 发送默认消息
        SpringUtil.getBean(MessageService.class).sendCreateDefaultMessage(
                startGroupChatDTO.getUserId(), roomInfoVO.getRoomId(), ImConstant.DEFAULT_GROUP_CHAT_MESSAGE
        );

        return roomInfoVO;
    }

    private void checkStartGroupChatParams(StartGroupChatDTO startGroupChatDTO) {
        AssertUtils.isNotEmpty(startGroupChatDTO, "参数不能为空");
        AssertUtils.isNotEmpty(startGroupChatDTO.getUserId(), "发起者不能为空");
        AssertUtils.isTrue(CollectionUtil.isNotEmpty(startGroupChatDTO.getUserIdList()), "邀请人列表不能为空");
    }

    @Override
    public List<ImUserVO> selectRoomMemberListByRoomId(RoomDTO roomDTO) {
        checkSelectRoomMemberListParams(roomDTO);
        List<Long> userIdList = selectRoomMemberIdListByRoomId(roomDTO.getRoomId());
        Map<Long, User> userMap = userCache.getBatch(userIdList);

        return userIdList.stream().map(item -> BeanUtil.copyProperties(userMap.get(item), ImUserVO.class)).collect(Collectors.toList());
    }

    private void checkSelectRoomMemberListParams(RoomDTO roomDTO) {
        AssertUtils.isNotEmpty(roomDTO, "参数不能为空");
        AssertUtils.isNotEmpty(roomDTO.getRoomId(), "房间ID不能为空");
    }
}
