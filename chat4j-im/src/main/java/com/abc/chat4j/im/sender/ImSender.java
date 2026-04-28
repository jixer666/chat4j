package com.abc.chat4j.im.sender;

import com.abc.chat4j.common.core.mq.redis.RedisMQTemplate;
import com.abc.chat4j.im.constant.ImQueueConstant;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/28
 */
@Component
public class ImSender {

    @Autowired
    private RedisMQTemplate redisMQTemplate;

    public <T> void sendMessage(ImSendContext<T> context) {



        redisMQTemplate.opsForList().rightPush(ImQueueConstant.MESSAGE_QUEUE, context);
    }
}
