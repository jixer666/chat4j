package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.FriendApplyMessage;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.ConversationCreateContext;
import com.abc.chat4j.platform.domain.context.FriendApplyQueryContext;
import com.abc.chat4j.platform.domain.context.RoomCreateContext;
import com.abc.chat4j.platform.domain.dto.FriendApplyDTO;
import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.entity.FriendApply;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.enums.FriendApplyStatusEnum;
import com.abc.chat4j.platform.domain.enums.RoomTypeEnum;
import com.abc.chat4j.platform.domain.vo.FriendApplyVO;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.abc.chat4j.platform.mapper.FriendApplyMapper;
import com.abc.chat4j.platform.service.*;
import com.abc.chat4j.system.cache.UserCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements FriendApplyService {

    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Autowired
    private UserCache userCache;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Override
    public FriendApplyVO sendFriendApply(FriendApplyDTO friendApplyDTO) {
        FriendApply friendApply = saveOrUpdateUserFriendApply(friendApplyDTO);
        sendFriendApplyMessage(friendApply);
        return BeanUtil.copyProperties(friendApply, FriendApplyVO.class);
    }

    private void sendFriendApplyMessage(FriendApply friendApply) {

    }

    private void checkUserFriendApplyDTOParams(FriendApplyDTO friendApplyDTO) {
        AssertUtils.isNotEmpty(friendApplyDTO, "好友请求参数不能为空");
        AssertUtils.isNotEmpty(friendApplyDTO.getFriendId(), "好友ID不能为空");
    }

    private FriendApply saveOrUpdateUserFriendApply(FriendApplyDTO friendApplyDTO) {
        checkUserFriendApplyDTOParams(friendApplyDTO);

        FriendApply friendApply = new FriendApply();
        FriendApply findUserApply = selectFriendApplyByUserIdAndFriendId(friendApplyDTO.getUserId(), friendApplyDTO.getFriendId());
        if (Objects.nonNull(findUserApply)) {
            AssertUtils.isFalse(FriendApplyStatusEnum.PENDING.getStatus().equals(findUserApply.getStatus()), "已发送好友申请，请勿重复");
            AssertUtils.isFalse(FriendApplyStatusEnum.ACCEPT.getStatus().equals(findUserApply.getStatus()), "已是好友关系，请勿重复");
            // 已拒绝的申请变为待处理
            friendApply.setStatus(FriendApplyStatusEnum.PENDING.getStatus());
            friendApplyMapper.updateById(friendApply);
        } else {
            friendApply.setUserId(friendApplyDTO.getUserId());
            friendApply.setFriendId(friendApplyDTO.getFriendId());
            friendApply.setRemark(friendApplyDTO.getRemark());
            friendApply.setCommonParams();
            friendApplyMapper.insert(friendApply);
        }

        return friendApply;
    }

    @Override
    public FriendApply selectFriendApplyByUserIdAndFriendId(Long userId, Long friendId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(userId, "好友ID不能为空");

        FriendApplyQueryContext context = new FriendApplyQueryContext();
        context.setUserId(userId);
        context.setFriendId(friendId);

        return selectFriendApply(context).get(0);
    }

    @Override
    public List<FriendApplyVO> selectOfflineFriendApplyList(FriendApplyPullDTO friendApplyPullDTO) {
        checkFriendApplyPullDTOParams(friendApplyPullDTO);

        FriendApplyQueryContext context = new FriendApplyQueryContext();
        // 查询自己申请的好友申请
        context.setUserId(SecurityUtils.getUserId());
        context.setMinUpdateTime(friendApplyPullDTO.getMinUpdateTime());
        List<FriendApply> friendApplyList = selectFriendApply(context);
        // 查询别人申请，申请对方是自己的好友申请
        context = new  FriendApplyQueryContext();
        context.setFriendId(SecurityUtils.getUserId());
        context.setMinUpdateTime(friendApplyPullDTO.getMinUpdateTime());
        friendApplyList.addAll(selectFriendApply(context));

        return buildFriendApplyVOList(friendApplyList);
    }

    private List<FriendApplyVO> buildFriendApplyVOList(List<FriendApply> friendApplyList) {
        Set<Long> userIdSet = friendApplyList.stream().map(item -> SecurityUtils.getUserId().equals(item.getUserId()) ? item.getFriendId() : item.getUserId()).collect(Collectors.toSet());
        Map<Long, User> userMap = userCache.getBatch(new ArrayList<>(userIdSet));
        return friendApplyList.stream().map(item -> buildfriendApplyVO(item, userMap)).collect(Collectors.toList());
    }

    private FriendApplyVO buildfriendApplyVO(FriendApply friendApply, Map<Long, User> userMap) {
        FriendApplyVO friendApplyVO = new FriendApplyVO();
        friendApplyVO.setUserId(friendApply.getUserId());
        friendApplyVO.setFriendId(friendApply.getFriendId());
        friendApplyVO.setRemark(friendApply.getRemark());
        friendApplyVO.setFriendApplyId(friendApply.getFriendApplyId());
        friendApplyVO.setStatus(friendApply.getStatus());
        friendApplyVO.setCreateTime(friendApply.getCreateTime());

        User user = userMap.get(SecurityUtils.getUserId().equals(friendApply.getUserId()) ? friendApply.getFriendId() : friendApply.getUserId());
        if (Objects.nonNull(user)) {
            ImUserVO imUserVO = new ImUserVO();
            imUserVO.setUserId(user.getUserId());
            imUserVO.setUsername(user.getUsername());
            imUserVO.setAvatar(user.getAvatar());
            imUserVO.setNickname(user.getNickname());
            friendApplyVO.setUserInfo(imUserVO);
        }

        return friendApplyVO;
    }

    private List<FriendApply> selectFriendApply(FriendApplyQueryContext context) {
        return friendApplyMapper.selectFriendApply(context);
    }

    private void checkFriendApplyPullDTOParams(FriendApplyPullDTO friendApplyPullDTO) {
        AssertUtils.isNotEmpty(friendApplyPullDTO, "参数不能为空");
        // 申请最大取30天内的
        Date minUpdateTime = friendApplyPullDTO.getMinUpdateTime();
        Date maxMinUpdateTime = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_FRIEND_APPLY_DAYS));
        friendApplyPullDTO.setMinUpdateTime(Objects.isNull(minUpdateTime) ? maxMinUpdateTime :
                minUpdateTime.before(maxMinUpdateTime) ? maxMinUpdateTime : minUpdateTime);
    }

    @Override
    public FriendApplyVO operationFriendApply(FriendApplyDTO friendApplyDTO) {
        checkOperationFriendApplyParams(friendApplyDTO);

        FriendApplyQueryContext context = new FriendApplyQueryContext();
        context.setFriendApplyId(friendApplyDTO.getFriendApplyId());
        List<FriendApply> friendApplyList = selectFriendApply(context);
        AssertUtils.isTrue(CollectionUtil.isNotEmpty(friendApplyList) && friendApplyList.size() == 1, "该好友申请不存在");
        FriendApply friendApply = friendApplyList.get(0);
        AssertUtils.isTrue(friendApply.getFriendId().equals(SecurityUtils.getUserId()), "无权限处理该好友申请");
        AssertUtils.isTrue(FriendApplyStatusEnum.PENDING.getStatus().equals(friendApply.getStatus()), "该好友申请已经处理，请勿重复");

        transactionTemplate.execute(item -> {
            if (FriendApplyDTO.ACCEPT.equals(friendApplyDTO.getOperationType())) {
                friendApply.setStatus(FriendApplyStatusEnum.ACCEPT.getStatus());
                // 添加好友关系
                saveUserFriend(friendApply);
            } else {
                friendApply.setStatus(FriendApplyStatusEnum.REJECT.getStatus());
            }
            friendApplyMapper.updateById(friendApply);

            return null;
        });

        // 推送申请处理消息
        sendFriendApplyOperationMessage(friendApply);
        if (FriendApplyDTO.ACCEPT.equals(friendApplyDTO.getOperationType())) {
            // 创建会话，主动推送一条消息
            sendConversationMessage(friendApply);
        }

        return buildfriendApplyVO(friendApply, new HashMap<>());
    }

    private void sendConversationMessage(FriendApply friendApply) {
        // 创建私聊房间
        RoomCreateContext roomContext = new RoomCreateContext();
        roomContext.setType(RoomTypeEnum.PRIVATE.getType());
        roomContext.setUid1(friendApply.getFriendId());
        roomContext.setUid2(friendApply.getUserId());
        RoomInfoVO roomInfo = roomService.createRoom(roomContext);
        // 创建会话
        ConversationCreateContext conversationContext = new ConversationCreateContext();
        conversationContext.setRoomId(roomInfo.getRoomId());
        conversationContext.setUserIdList(CollectionUtil.newArrayList(friendApply.getUserId(), friendApply.getFriendId()));
        conversationService.createConversation(conversationContext);
        // 好友同意好友申请，主动推送一条消息
        messageService.sendCreateDefaultMessage(friendApply.getFriendId(), roomInfo.getRoomId(), friendApply.getRemark());
    }

    private void saveUserFriend(FriendApply friendApply) {
        UserFriend user = new UserFriend();
        user.setUserId(friendApply.getUserId());
        user.setFriendId(friendApply.getFriendId());
        UserFriend friend = new UserFriend();
        friend.setUserId(friendApply.getFriendId());
        friend.setFriendId(friendApply.getUserId());

        userFriendService.saveUserFriendList(Arrays.asList(
                user, friend
        ));
    }

    private void sendFriendApplyOperationMessage(FriendApply friendApply) {
        MessageProcess<?> messageProcess = MessageProcessFactory.getService(ImMessageTypeEnum.FRIEND_APPLY.getType());

        ImSendContext<FriendApplyMessage> context = new ImSendContext<>();
        context.setImSendUserInfo(new ImSendUserInfo(SecurityUtils.getUserId(), SecurityUtils.getLoginUser().getDevice()));
        context.setTargetUserIdList(CollectionUtil.newArrayList(friendApply.getUserId()));

        FriendApplyMessage friendApplyMessage = new FriendApplyMessage();
        friendApplyMessage.setFriendId(friendApply.getFriendId());
        friendApplyMessage.setFriendApplyId(friendApply.getFriendApplyId());
        friendApplyMessage.setFriendId(friendApply.getFriendId());
        friendApplyMessage.setUserId(friendApply.getUserId());
        friendApplyMessage.setStatus(friendApply.getStatus());

        context.setData(friendApplyMessage);
        context.setQueue(ImQueueConstant.FRIEND_APPLY_OPERATION_QUEUE);
        messageProcess.process(context);
    }

    private void checkOperationFriendApplyParams(FriendApplyDTO friendApplyDTO) {
        AssertUtils.isNotEmpty(friendApplyDTO, "参数不能为空");
        AssertUtils.isNotEmpty(friendApplyDTO.getFriendApplyId(), "申请ID不能为空");
    }
}
