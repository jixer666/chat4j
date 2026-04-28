package com.abc.chat4j.platform.listener;

import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import com.abc.chat4j.im.constant.ImQueueConstant;
import com.abc.chat4j.platform.domain.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Slf4j
@Component
@RedisMQListener(queue = ImQueueConstant.MESSAGE_QUEUE)
public class MessageListener extends RedisMQConsumer<Message> {

    @Override
    public void onMessage(List<Message> dataList) {
        log.info("【消费消息】开始，数量：{}", dataList.size());

        log.info("【消费消息】结束");
    }
}
