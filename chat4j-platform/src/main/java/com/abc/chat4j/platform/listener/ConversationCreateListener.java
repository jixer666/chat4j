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
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/5/10
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.CONVERSATION_QUEUE)
public class ConversationCreateListener extends RedisMQConsumer<ImReceiveContext> {

    @Override
    public void onMessage(List<ImReceiveContext> dataList) {
        log.info("【conversationQueue】【消费消息】开始，数量：{}", dataList.size());
        for (ImReceiveContext messageImReceiveContext : dataList) {
            ImSendUserInfo receiveUserInfo = messageImReceiveContext.getImReceiveUserInfo();
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiveUserInfo.getUserId(), receiveUserInfo.getDevice());
            if (Objects.isNull(channelCtx)) {
                continue;
            }
            ConversationVO conversationVO = BeanUtil.copyProperties(messageImReceiveContext.getData(), ConversationVO.class);
            ImSendInfo imSendInfo = new ImSendInfo();
            imSendInfo.setContent(conversationVO);
            imSendInfo.setType(ImMessageTypeEnum.CONVERSATION_CREATE.getType());
            imSendInfo.setUserId(conversationVO.getUserId());
            channelCtx.channel().writeAndFlush(imSendInfo);
        }
        log.info("【conversationQueue】【消费消息】结束");
    }
}
