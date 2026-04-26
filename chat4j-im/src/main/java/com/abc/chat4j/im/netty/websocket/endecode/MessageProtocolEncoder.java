package com.abc.chat4j.im.netty.websocket.endecode;

import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class MessageProtocolEncoder extends MessageToMessageEncoder<ImSendInfo> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImSendInfo sendInfo, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String text = objectMapper.writeValueAsString(sendInfo);
        TextWebSocketFrame frame = new TextWebSocketFrame(text);
        list.add(frame);
    }
}
