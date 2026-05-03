package com.abc.chat4j.platform.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.core.mq.redis.RedisMQTemplate;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.ImCallbackContext;
import com.abc.chat4j.im.netty.process.model.ImReceiveContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.platform.domain.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.MESSAGE_QUEUE)
public class MessageListener extends RedisMQConsumer<ImReceiveContext> {

    @Autowired
    private RedisMQTemplate redisMQTemplate;

    @Override
    public void onMessage(List<ImReceiveContext> dataList) {
        log.info("【messageQueue】【消费消息】开始，数量：{}", dataList.size());
        for (ImReceiveContext messageImReceiveContext : dataList) {
            ImSendUserInfo receiveUserInfo = messageImReceiveContext.getImReceiveUserInfo();
            Message message = BeanUtil.copyProperties(messageImReceiveContext.getData(), Message.class);
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiveUserInfo.getUserId(), receiveUserInfo.getDevice());
            if (Objects.nonNull(channelCtx)) {
                ImSendInfo imSendInfo = new ImSendInfo();
                imSendInfo.setContent(message.getContent());
                imSendInfo.setType(message.getType());
                imSendInfo.setMsgId(message.getMsgId());
                imSendInfo.setRoomId(message.getRoomId());
                imSendInfo.setTempMsgId(message.getTempMsgId());
                imSendInfo.setUserId(message.getUserId());
                imSendInfo.setUserInfo(message.getUserInfo());
                imSendInfo.setCreateTime(message.getCreateTime());
                channelCtx.channel().writeAndFlush(imSendInfo);
            }
            ImCallbackContext imCallbackContext = new ImCallbackContext();
            imCallbackContext.setMsgId(message.getMsgId());
            imCallbackContext.setRoomId(message.getRoomId());
            imCallbackContext.setActiveTime(message.getCreateTime());
            // 消息回调
            redisMQTemplate.opsForList().rightPush(ImQueueConstant.getQueueKey(ImQueueConstant.MESSAGE_CALLBACK_QUEUE), imCallbackContext);
        }
        log.info("【messageQueue】【消费消息】结束");
    }
}
