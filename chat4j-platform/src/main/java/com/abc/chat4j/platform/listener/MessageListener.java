package com.abc.chat4j.platform.listener;

import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.ImReceiveContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import com.abc.chat4j.platform.domain.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    @Override
    public void onMessage(List<ImReceiveContext> dataList) {
        log.info("【messageQueue】【消费消息】开始，数量：{}", dataList.size());
        for (ImReceiveContext messageImReceiveContext : dataList) {
            ImSendUserInfo receiveUserInfo = messageImReceiveContext.getImReceiveUserInfo();
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiveUserInfo.getUserId(), receiveUserInfo.getDevice());
            if (Objects.isNull(channelCtx)) {
                continue;
            }
            channelCtx.channel().writeAndFlush(messageImReceiveContext.getData());
        }
        log.info("【messageQueue】【消费消息】结束");
    }
}
