package com.abc.chat4j.im.netty.process.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImSendContext {

    private ChannelHandlerContext ctx;

    private Object data;

    public ImSendContext(Object data) {
        this.data = data;
    }
}
