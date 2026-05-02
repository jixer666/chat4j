package com.abc.chat4j.platform.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.FriendApplyMessage;
import com.abc.chat4j.im.netty.process.model.ImReceiveContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.FRIEND_APPLY_OPERATION_QUEUE)
public class FriendApplyListener extends RedisMQConsumer<ImReceiveContext> {

    @Override
    public void onMessage(List<ImReceiveContext> dataList) {
        log.info("【friendApplyOperationQueue】【消费消息】开始，数量：{}", dataList.size());
        for (ImReceiveContext messageImReceiveContext : dataList) {
            ImSendUserInfo receiveUserInfo = messageImReceiveContext.getImReceiveUserInfo();
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiveUserInfo.getUserId(), receiveUserInfo.getDevice());
            if (Objects.isNull(channelCtx)) {
                continue;
            }
            FriendApplyMessage friendApplyMessage = BeanUtil.copyProperties(messageImReceiveContext.getData(), FriendApplyMessage.class);
            ImSendInfo imSendInfo = new ImSendInfo();
            imSendInfo.setData(friendApplyMessage);
            imSendInfo.setType(ImMessageTypeEnum.FRIEND_APPLY.getType());
            imSendInfo.setUserId(friendApplyMessage.getUserId());
            channelCtx.channel().writeAndFlush(imSendInfo);
        }
        log.info("【friendApplyOperationQueue】【消费消息】结束");
    }
}
