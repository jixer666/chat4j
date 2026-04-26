package com.abc.chat4j.im.netty;

import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class NettyUtil {

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static AttributeKey<LoginUserDTO> LOGIN_USER = AttributeKey.valueOf("loginUserDTO");
    public static AttributeKey<Integer> DEVICE = AttributeKey.valueOf("device");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        Attribute<T> attr = channel.attr(attributeKey);
        attr.set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> attributeKey) {
        return channel.attr(attributeKey).get();
    }
}