package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.*;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.context.MessageQueryContext;
import com.abc.chat4j.platform.domain.dto.MessageReadCountDTO;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.im.domain.entity.MessageUserInfo;
import com.abc.chat4j.platform.domain.enums.MessageStatusEnum;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.MessageReadUserVO;
import com.abc.chat4j.platform.domain.vo.MessageVO;
import com.abc.chat4j.platform.mapper.MessageMapper;
import com.abc.chat4j.platform.service.ConversationService;
import com.abc.chat4j.platform.service.MessageReadService;
import com.abc.chat4j.platform.service.MessageService;
import com.abc.chat4j.platform.service.RoomService;
import com.abc.chat4j.system.cache.UserCache;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserCache userCache;

    @Resource
    private ConversationService conversationService;

    @Autowired
    private MessageReadService messageReadService;

    @Override
    public List<MessageVO> selectOfflineMessageList(MessagePullDTO messagePullDTO) {
        checkMessagePullDTOParams(messagePullDTO);

        // 查询会话
        ConversationPullDTO conversationPullDTO = new ConversationPullDTO();
        conversationPullDTO.setMinUpdateTime(messagePullDTO.getMinUpdateTime());
        List<ConversationVO> conversationVOList = conversationService.selectConversationList(conversationPullDTO);
        Set<Long> roomIdSet = conversationVOList.stream().map(item -> item.getRoomInfo().getRoomId()).collect(Collectors.toSet());
        // 查询会话中的消息
        MessageQueryContext context = new MessageQueryContext();
        context.setMinUpdateTime(messagePullDTO.getMinUpdateTime());
        context.setRoomIdList(new ArrayList<>(roomIdSet));
        List<Message> messageList = selectMessage(context);

        return buildMessageVOList(messageList);
    }

    private List<MessageVO> buildMessageVOList(List<Message> messageList) {
        // 统计回执消息读取数量
        List<Long> recepitMsgIdList = messageList.stream().filter(item -> CommonConstants.YES.equals(item.getIsReceipt())).map(Message::getMsgId).collect(Collectors.toList());
        List<MessageReadCountDTO> msgReadCountDTOList = messageReadService.selectReadCountByMsgIdList(recepitMsgIdList);
        Map<Long, Integer> msgReadCountMap = msgReadCountDTOList.stream().collect(Collectors.toMap(MessageReadCountDTO::getMsgId, MessageReadCountDTO::getReadCount));

        return messageList.stream().map(item -> {
            MessageVO messageVO = BeanUtil.copyProperties(item, MessageVO.class);
            messageVO.setReadCount(msgReadCountMap.getOrDefault(item.getMsgId(), CommonConstants.ZERO));
            return messageVO;
        }).collect(Collectors.toList());
    }

    private List<Message> selectMessage(MessageQueryContext context) {
        return messageMapper.selectMessageList(context);
    }

    private void checkMessagePullDTOParams(MessagePullDTO messagePullDTO) {
        AssertUtils.isNotEmpty(messagePullDTO, "参数不能为空");
        // 消息最大取30天内的
        Date minUpdateTime = messagePullDTO.getMinUpdateTime();
        Date maxMinUpdateTime = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_MESSAGE_DAYS));
        messagePullDTO.setMinUpdateTime(Objects.isNull(minUpdateTime) ? maxMinUpdateTime :
                minUpdateTime.before(maxMinUpdateTime) ? maxMinUpdateTime : minUpdateTime);
    }

    @Override
    public MessageVO sendMessage(ImSendInfo imSendInfo) {
        checkSendMessageParams(imSendInfo);

        MessageProcess<?> messageProcess = MessageProcessFactory.getService(imSendInfo.getType());
        messageProcess.checkMessageContent(imSendInfo.getContent());

        Message message = saveSendMessage(imSendInfo);

        ImSendContext<Message> imSendContext = buildImSendContext(message);
        messageProcess.process(imSendContext);

        return BeanUtil.copyProperties(message, MessageVO.class);
    }

    private ImSendContext<Message> buildImSendContext(Message message) {
        ImSendContext<Message> imSendContext = new ImSendContext<>();
        imSendContext.setData(message);
        List<Long> userIdList = roomService.selectRoomMemberIdListByRoomId(message.getRoomId());
        List<Long> finalUserIdList = userIdList.stream().filter(item -> !item.equals(message.getUserId())).collect(Collectors.toList());
        imSendContext.setTargetUserIdList(finalUserIdList);
        imSendContext.setImSendUserInfo(new ImSendUserInfo(message.getUserId(), SecurityUtils.getLoginUser().getDevice()));

        return imSendContext;
    }

    private Message saveSendMessage(ImSendInfo imSendInfo) {
        Message message = new Message();
        message.setMsgId(IdUtils.getId());
        message.setType(imSendInfo.getType());
        message.setUserId(SecurityUtils.getUserId());
        message.setContent(JSONUtil.toJsonStr(imSendInfo.getContent()));
        message.setRoomId(imSendInfo.getRoomId());
        message.setTempMsgId(imSendInfo.getTempMsgId());
        User user = userCache.get(imSendInfo.getUserId());
        message.setUserInfo(new MessageUserInfo(user.getUserId(), user.getUsername(), user.getNickname(), user.getAvatar()));
        message.setIsReceipt(Objects.isNull(imSendInfo.getIsReceipt()) ? CommonConstants.NO : imSendInfo.getIsReceipt());
        message.setIsWithdrawn(Objects.isNull(imSendInfo.getIsWithdrawn()) ?  CommonConstants.NO : imSendInfo.getIsWithdrawn());
        message.setCommonParams();
        message.setStatus(MessageStatusEnum.PENDING.getStatus());

        messageMapper.insert(message);

        return message;
    }

    private void checkSendMessageParams(ImSendInfo imSendInfo) {
        AssertUtils.isNotEmpty(imSendInfo, "发送消息参数不能为空");
        AssertUtils.isNotEmpty(imSendInfo.getRoomId(), "消息房间不能为空");
        AssertUtils.isNotEmpty(imSendInfo.getType(), "消息类型不能为空");
        ImMessageTypeEnum imMessageTypeEnum = ImMessageTypeEnum.typeOf(imSendInfo.getType());
        AssertUtils.isNotEmpty(imMessageTypeEnum, "未知消息类型");
        roomService.checkUserInRoom(imSendInfo.getUserId(), imSendInfo.getRoomId());
    }

    @Override
    public void readMessage(MessageReadDTO messageReadDTO) {
        checkMessageReadDTOParams(messageReadDTO);
        if (MessageReadDTO.READ_MESSAGE.equals(messageReadDTO.getType())) {
            // 消息消息级读取
            List<Long> msgIdList = messageReadService.markMessageRead(messageReadDTO);
            if (CollectionUtil.isEmpty(msgIdList)) {
                return;
            }
            // 读取消息推送
            sendReadMessage(messageReadDTO.getUserId(), messageReadDTO.getDevice(), messageReadDTO.getRoomId(), msgIdList);
        } else {
            // 会话级读取
            conversationService.updateActiveTimeByConversationId(messageReadDTO.getConversationId(), messageReadDTO.getUserId(), new Date());
        }
    }

    private void sendReadMessage(Long userId, Integer device, Long roomId, List<Long> msgIdList) {
        MessageProcess<?> messageProcess = MessageProcessFactory.getService(ImMessageTypeEnum.MESSAGE_READ.getType());

        ImSendContext<ReadMessage> context = new ImSendContext<>();
        context.setImSendUserInfo(new ImSendUserInfo(userId, device));

        List<Long> userIdList = roomService.selectRoomMemberIdListByRoomId(roomId);
        List<Long> finalUserIdList = userIdList.stream().filter(item -> !item.equals(userId)).collect(Collectors.toList());
        context.setTargetUserIdList(finalUserIdList);

        ReadMessage readMessage = new ReadMessage(userId, roomId, msgIdList);

        context.setData(readMessage);
        context.setQueue(ImQueueConstant.MESSAGE_READ_QUEUE);

        messageProcess.process(context);
    }

    private void checkMessageReadDTOParams(MessageReadDTO messageReadDTO) {
        AssertUtils.isNotEmpty(messageReadDTO.getType(), "读取消息类型不能为空");
        AssertUtils.isTrue(messageReadDTO.getType().equals(MessageReadDTO.READ_MESSAGE) ||
                messageReadDTO.getType().equals(MessageReadDTO.READ_CONVERSATION), "读取消息类型不正确");
        if (MessageReadDTO.READ_MESSAGE.equals(messageReadDTO.getType())) {
            AssertUtils.isTrue(CollectionUtil.isNotEmpty(messageReadDTO.getMsgIdList()), "读取消息列表不能为空");
            AssertUtils.isNotEmpty(messageReadDTO.getRoomId(), "房间Id不能为空");
        } else {
            AssertUtils.isTrue(Objects.nonNull(messageReadDTO.getConversationId()), "读取会话不能为空");
        }
    }

    @Override
    public void sendCreateDefaultMessage(Long userId, Long roomId, String message) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");

        ImSendInfo imSendInfo = new ImSendInfo();
        imSendInfo.setRoomId(roomId);
        imSendInfo.setUserId(userId);
        imSendInfo.setType(ImMessageTypeEnum.TEXT.getType());
        imSendInfo.setTempMsgId(IdUtils.getId());
        TextMessage textMessage = new TextMessage();
        textMessage.setText(StringUtils.isEmpty(message) ? ImConstant.DEFAULT_CONVERSATION_MESSAGE : message);
        imSendInfo.setContent(textMessage);

        sendMessage(imSendInfo);
    }

    @Override
    public void updateStatusByMsgIdList(Integer status, List<Long> msgIdList) {
        if (CollectionUtils.isEmpty(msgIdList)) {
            return;
        }
        messageMapper.updateStatusByMsgIdList(status, msgIdList);
    }

    @Override
    public List<Long> selectRecepitMessageByMsgIdList(List<Long> msgIdList) {
        if (CollectionUtils.isEmpty(msgIdList)) {
            return new ArrayList<>();
        }

        return messageMapper.selectReceiptMessageByMsgIdList(CommonConstants.YES, msgIdList);
    }

    @Override
    public MessageReadUserVO selectReadUserInfo(MessageReadDTO messageReadDTO) {
        checkMessageReadUserInfoDTOParams(messageReadDTO);

        MessageReadUserVO messageReadUserVO = new MessageReadUserVO();
        // 查询已读人员ID列表
        Set<Long> readUserIdSet = new HashSet<>(messageReadService.selectReadUserIdListByMsgId(messageReadDTO.getMsgId()));
        List<Long> groupUserIdList = roomService.selectRoomMemberIdListByRoomId(messageReadDTO.getRoomId());
        // 查询未读人员ID列表
        List<Long> unReadUserIdList = groupUserIdList.stream().filter(item -> !readUserIdSet.contains(item) && !item.equals(SecurityUtils.getUserId())).collect(Collectors.toList());
        List<Long> allUserIdList = Lists.newArrayList(readUserIdSet);
        allUserIdList.addAll(unReadUserIdList);
        Map<Long, User> userMap = userCache.getBatch(allUserIdList);

        List<ImUserVO> readUserList = readUserIdSet.stream().map(item -> {
            User user = userMap.get(item);
            return BeanUtil.copyProperties(user, ImUserVO.class);
        }).collect(Collectors.toList());
        List<ImUserVO> unReadUserList = unReadUserIdList.stream().map(item -> {
            User user = userMap.get(item);
            return BeanUtil.copyProperties(user, ImUserVO.class);
        }).collect(Collectors.toList());
        messageReadUserVO.setReadUserList(readUserList);
        messageReadUserVO.setUnReadUserList(unReadUserList);

        return messageReadUserVO;
    }

    private void checkMessageReadUserInfoDTOParams(MessageReadDTO messageReadDTO) {
        AssertUtils.isNotEmpty(messageReadDTO, "参数不能为空");
        AssertUtils.isNotEmpty(messageReadDTO.getMsgId(), "消息ID不能为空");
        AssertUtils.isNotEmpty(messageReadDTO.getRoomId(), "房间ID不能为空");
    }

    @Override
    public void sendCreateConversationMessage(ConversationVO conversationVO, Long userId) {
        MessageProcess<?> messageProcess = MessageProcessFactory.getService(ImMessageTypeEnum.CONVERSATION_CREATE.getType());

        ImSendContext<ConversationVO> context = new ImSendContext<>();
        context.setImSendUserInfo(new ImSendUserInfo(SecurityUtils.getUserId(), SecurityUtils.getLoginUser().getDevice()));
        context.setTargetUserIdList(Lists.newArrayList(userId));
        context.setData(conversationVO);
        context.setQueue(ImQueueConstant.CONVERSATION_QUEUE);

        messageProcess.process(context);
    }
}
