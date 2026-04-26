package com.abc.chat4j.im.netty;

import cn.hutool.core.net.url.UrlBuilder;
import com.abc.chat4j.common.domain.enums.DeviceEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Optional;

/**
 * 连接请求头处理，目前暂未使用
 */
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            // 获取token参数
            String token = Optional.ofNullable(urlBuilder.getQuery())
                    .map(k->k.get("token"))
                    .map(CharSequence::toString)
                    .orElse(null);
            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);
            // 获取设备编号
            Integer device = Optional.ofNullable(urlBuilder.getQuery())
                    .map(k->k.get("device"))
                    .map(CharSequence::toString)
                    .map(Integer::valueOf)
                    .orElse(DeviceEnum.WEB.getType());
            NettyUtil.setAttr(ctx.channel(), NettyUtil.DEVICE, device);

            ctx.pipeline().remove(this);
            ctx.fireChannelRead(request);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}