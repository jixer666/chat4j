package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.MessageQueryContext;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.platform.domain.vo.MessageVO;
import com.abc.chat4j.platform.mapper.MessageMapper;
import com.abc.chat4j.platform.service.MessageService;
import com.abc.chat4j.platform.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<MessageVO> selectOfflineMessageList(MessagePullDTO messagePullDTO) {
        checkMessagePullDTOParams(messagePullDTO);

        MessageQueryContext context = new MessageQueryContext();
        context.setUserId(SecurityUtils.getUserId());
        context.setBegin(DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_MESSAGE_DAYS)));
        context.setEnd(new Date());
        context.setMsgId(messagePullDTO.getMinMsgId());
        List<Message> messageList = selectMessage(context);

        return BeanUtil.copyToList(messageList, MessageVO.class);
    }

    private List<Message> selectMessage(MessageQueryContext context) {
        return messageMapper.selectMessageList(context);
    }

    private void checkMessagePullDTOParams(MessagePullDTO messagePullDTO) {
        AssertUtils.isNotEmpty(messagePullDTO, "参数不能为空");
        if (Objects.isNull(messagePullDTO.getMinMsgId())) {
            messagePullDTO.setMinMsgId(0L);
        }
    }

    @Override
    public MessageVO sendMessage(ImSendInfo imSendInfo) {
        checkSendMessageParams(imSendInfo);
        MessageProcess<?> messageProcess = MessageProcessFactory.getService(imSendInfo.getType());
        ImSendContext imSendContext = new ImSendContext(imSendInfo.getData());
        messageProcess.process(imSendContext);
        return null;
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
