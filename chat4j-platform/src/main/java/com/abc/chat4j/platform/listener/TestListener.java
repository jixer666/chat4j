package com.abc.chat4j.platform.listener;

import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.core.mq.redis.RedisMQConsumer;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Component
@RedisMQListener(queue = "test")
public class TestListener extends RedisMQConsumer<Object> {


}
