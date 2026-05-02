package com.abc.chat4j.im.netty.process.model;

import com.abc.chat4j.common.constant.ImQueueConstant;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Data
@NoArgsConstructor
public class ImSendContext<T> {

    private ChannelHandlerContext ctx;

    // 消息实体
    private T data;

    // 发送方用户
    private ImSendUserInfo imSendUserInfo;

    // 目标方用户ID列表
    private List<Long> targetUserIdList;

    // 消息内容
    private Object msgData;

    // 消息发送的队列
    private String queue = ImQueueConstant.MESSAGE_QUEUE;

    public ImSendContext(ChannelHandlerContext ctx, Object msgData) {
        this.ctx = ctx;
        this.msgData = msgData;
    }
}
