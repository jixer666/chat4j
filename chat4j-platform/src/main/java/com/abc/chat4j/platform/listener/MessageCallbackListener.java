package com.abc.chat4j.platform.listener;

import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.im.netty.process.model.ImCallbackContext;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.enums.MessageStatusEnum;
import com.abc.chat4j.platform.service.ConversationService;
import com.abc.chat4j.platform.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/3
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.MESSAGE_CALLBACK_QUEUE)
public class MessageCallbackListener extends RedisMQConsumer<ImCallbackContext> {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(List<ImCallbackContext> dataList) {
        log.info("【messageCallBackQueue】【消费消息】开始，数量：{}", dataList.size());
        List<Conversation> conversationList = new ArrayList();
        List<Long> msgIdList = new ArrayList();
        for (ImCallbackContext imCallbackContext : dataList) {
            msgIdList.add(imCallbackContext.getMsgId());

            Conversation conversation = new Conversation();
            conversation.setRoomId(imCallbackContext.getRoomId());
            conversation.setLastMsgId(imCallbackContext.getMsgId());
            conversation.setUpdateTime(imCallbackContext.getActiveTime());
            conversation.setActiveTime(imCallbackContext.getActiveTime());
            conversationList.add(conversation);
        }
        conversationService.updateActiveTimeBatchByRoomId(conversationList);
        messageService.updateStatusByMsgIdList(MessageStatusEnum.SENT.getStatus(), msgIdList);
        log.info("【messageCallBackQueue】【消费消息】结束");

    }
}
