package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.im.netty.process.model.TextMessage;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.context.MessageQueryContext;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.im.domain.entity.MessageUserInfo;
import com.abc.chat4j.platform.domain.enums.MessageStatusEnum;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.abc.chat4j.platform.domain.vo.MessageVO;
import com.abc.chat4j.platform.mapper.MessageMapper;
import com.abc.chat4j.platform.service.ConversationService;
import com.abc.chat4j.platform.service.MessageService;
import com.abc.chat4j.platform.service.RoomService;
import com.abc.chat4j.system.cache.UserCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

        return BeanUtil.copyToList(messageList, MessageVO.class);
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
        List<Long> userIdList = roomService.getRoomMemberListByRoomId(message.getRoomId());
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
            // todo 暂未实现消息消息级读取
            if (CollectionUtil.isEmpty(messageReadDTO.getMsgIdList())) {
                return;
            }

        } else {
            // 会话级读取
            conversationService.updateActiveTimeByConversationId(messageReadDTO.getConversationId(), messageReadDTO.getUserId(), new Date());
        }
    }

    private void checkMessageReadDTOParams(MessageReadDTO messageReadDTO) {
        AssertUtils.isNotEmpty(messageReadDTO.getType(), "读取消息类型不能为空");
        AssertUtils.isTrue(messageReadDTO.getType().equals(MessageReadDTO.READ_MESSAGE) ||
                messageReadDTO.getType().equals(MessageReadDTO.READ_CONVERSATION), "读取消息类型不正确");
        AssertUtils.isTrue(MessageReadDTO.READ_MESSAGE.equals(messageReadDTO.getType()) &&
                Objects.nonNull(messageReadDTO.getMsgIdList()), "读取消息列表不能为空");
        AssertUtils.isTrue(MessageReadDTO.READ_CONVERSATION.equals(messageReadDTO.getType()) &&
                Objects.nonNull(messageReadDTO.getConversationId()), "读取会话不能为空");
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
}
