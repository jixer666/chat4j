package com.abc.chat4j.im.netty;

import com.abc.chat4j.im.constant.ChannelAttrKey;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.factory.MessageProcessFactory;
import com.abc.chat4j.im.netty.process.MessageProcess;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket 长连接下 文本帧的处理器 实现浏览器发送文本回写 浏览器连接状态监控
 */
@Slf4j
public class ImChannelHandler extends SimpleChannelInboundHandler<ImSendInfo> {

    /**
     * 读取到消息后进行处理
     *
     * @param ctx      channel上下文
     * @param sendInfo 发送消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImSendInfo sendInfo) {
        MessageProcess<?> messageProcess = MessageProcessFactory.getService(sendInfo.getType());
        ImSendContext imSendContext = new ImSendContext(ctx, sendInfo.getData());
        messageProcess.process(imSendContext);
    }

    /**
     * 出现异常的处理 打印报错日志
     *
     * @param ctx   channel上下文
     * @param cause 异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("websocket连接发生错误：{}", cause.getMessage(), cause);
        //关闭上下文
        ctx.close();
    }

    /**
     * 监控浏览器上线
     *
     * @param ctx channel上下文
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("{}连接", ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
//        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
//        Long userId = ctx.channel().attr(userIdAttr).get();
//        AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
//        Integer terminal = ctx.channel().attr(terminalAttr).get();
//        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId, terminal);
//        String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
//        RedisMQTemplate redisTemplate = SpringContextHolder.getBean(RedisMQTemplate.class);
//        Object serverId = redisTemplate.opsForValue().get(key);
//        // 判断一下，避免异地登录导致的误删
//        Boolean isSameServer = IMServerGroup.serverId.equals(Long.parseLong(StrUtil.toString(serverId)));
//        if (isSameServer && !Objects.isNull(context) && ctx.channel().id().equals(context.channel().id())) {
//            // 移除channel
//            UserChannelCtxMap.removeChannelCtx(userId, terminal);
//            // 用户下线
//            redisTemplate.delete(key);
//            log.info("断开连接,userId:{},终端类型:{},{}", userId, terminal, ctx.channel().id().asLongText());
//        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                log.info("心跳超时，即将断开连接,用户id:{},设备类型:{} ",
                        NettyUtil.getAttr(ctx.channel(), NettyUtil.LOGIN_USER).getUserId(),
                        NettyUtil.getAttr(ctx.channel(), NettyUtil.DEVICE));
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}