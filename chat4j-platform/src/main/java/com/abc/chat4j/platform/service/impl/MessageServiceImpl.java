package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.context.MessageQueryContext;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.platform.domain.entity.MessageUserInfo;
import com.abc.chat4j.platform.domain.enums.MessageStatusEnum;
import com.abc.chat4j.platform.domain.vo.MessageVO;
import com.abc.chat4j.platform.mapper.MessageMapper;
import com.abc.chat4j.platform.service.MessageService;
import com.abc.chat4j.platform.service.RoomService;
import com.abc.chat4j.system.cache.UserCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Override
    public List<MessageVO> selectOfflineMessageList(MessagePullDTO messagePullDTO) {
        checkMessagePullDTOParams(messagePullDTO);

        MessageQueryContext context = new MessageQueryContext();
        context.setUserId(SecurityUtils.getUserId());
        context.setMinUpdateTime(messagePullDTO.getMinUpdateTime());
        List<Message> messageList = selectMessage(context);

        return BeanUtil.copyToList(messageList, MessageVO.class);
    }

    private List<Message> selectMessage(MessageQueryContext context) {
        return messageMapper.selectMessageList(context);
    }

    private void checkMessagePullDTOParams(MessagePullDTO messagePullDTO) {
        AssertUtils.isNotEmpty(messagePullDTO, "参数不能为空");
        if (Objects.isNull(messagePullDTO.getMinUpdateTime())) {
            // 若不存在更新时间，默认取30天内的
            messagePullDTO.setMinUpdateTime(DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_CONVERSATION_DAYS)));
        }
    }

    @Override
    public MessageVO sendMessage(ImSendInfo imSendInfo) {
        checkSendMessageParams(imSendInfo);

        MessageProcess<?> messageProcess = MessageProcessFactory.getService(imSendInfo.getType());
        messageProcess.checkMessageContent(imSendInfo.getData());

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
        message.setContent(JSONUtil.toJsonStr(imSendInfo.getData()));
        message.setRoomId(imSendInfo.getRoomId());
        message.setTempMsgId(imSendInfo.getTempMsgId());
        User user = userCache.get(imSendInfo.getUserId());
        message.setUserInfo(new MessageUserInfo(user.getNickname(), user.getAvatar()));
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
        roomService.checkUserInRoom(SecurityUtils.getUserId(), imSendInfo.getRoomId());
    }
}
