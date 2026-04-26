package com.abc.chat4j.im.netty.websocket.endecode;

import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class MessageProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ImSendInfo sendInfo = objectMapper.readValue(textWebSocketFrame.text(), ImSendInfo.class);
        list.add(sendInfo);
    }
}
