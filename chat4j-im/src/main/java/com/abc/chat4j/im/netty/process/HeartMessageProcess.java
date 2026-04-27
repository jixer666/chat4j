package com.abc.chat4j.im.netty.process;

import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/27
 */
@Component
@MessageType(type = ImMessageTypeEnum.HEART)
public class HeartMessageProcess extends MessageProcess<Object> {

    @Override
    public void doProcess(ChannelHandlerContext ctx, Object data) {
        super.doProcess(ctx, data);
    }
}
