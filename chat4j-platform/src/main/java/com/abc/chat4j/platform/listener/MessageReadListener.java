package com.abc.chat4j.platform.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.ImReceiveContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.im.netty.process.model.ReadMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/5/6
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.MESSAGE_READ_QUEUE)
public class MessageReadListener extends RedisMQConsumer<ImReceiveContext> {

    @Override
    public void onMessage(List<ImReceiveContext> dataList) {
        log.info("【messageReadListener】【消费消息】开始，数量：{}", dataList.size());
        for (ImReceiveContext messageImReceiveContext : dataList) {
            ImSendUserInfo receiveUserInfo = messageImReceiveContext.getImReceiveUserInfo();
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiveUserInfo.getUserId(), receiveUserInfo.getDevice());
            if (Objects.isNull(channelCtx)) {
                continue;
            }
            ReadMessage readMessage = BeanUtil.copyProperties(messageImReceiveContext.getData(), ReadMessage.class);
            ImSendInfo imSendInfo = new ImSendInfo();
            imSendInfo.setContent(readMessage);
            imSendInfo.setType(ImMessageTypeEnum.MESSAGE_READ.getType());
            imSendInfo.setUserId(readMessage.getUserId());
            channelCtx.channel().writeAndFlush(imSendInfo);
        }
        log.info("【messageReadListener】【消费消息】结束");
    }
}
