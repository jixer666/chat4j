package com.abc.chat4j.im.netty.websocket.endecode;

import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class MessageProtocolEncoder extends MessageToMessageEncoder<ImSendInfo> {

    private final ObjectMapper objectMapper;

    public MessageProtocolEncoder() {
        this.objectMapper = new ObjectMapper();
        // 设置时区为中国时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 配置日期格式（可选）
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 创建模块并注册 Long 类型的序列化器
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(long.class, ToStringSerializer.instance);
        objectMapper.registerModule(module);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImSendInfo sendInfo, List<Object> list) throws Exception {
        String text = objectMapper.writeValueAsString(sendInfo);
        TextWebSocketFrame frame = new TextWebSocketFrame(text);
        list.add(frame);
    }
}
