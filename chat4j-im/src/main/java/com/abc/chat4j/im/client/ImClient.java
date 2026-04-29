package com.abc.chat4j.im.client;

import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.im.sender.ImSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Component
public class ImClient {

    @Autowired
    private ImSender imSender;

    /**
     * 推送消息
     */
    public <T> void sendMessage(ImSendContext<T> context) {
        imSender.sendMessage(context);
    }

    /**
     * 是否在线
     */
    public boolean isOnline(Long userId, Integer device) {
        return imSender.isOnline(userId, device);
    }
}
