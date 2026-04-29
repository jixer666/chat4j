package com.abc.chat4j.common.constant;

import com.abc.chat4j.common.config.ServerConfig;

public class ImQueueConstant {

    public static final String MESSAGE_QUEUE = "messageQueue";


    public static String getQueueKey(String queue) {
        return CacheConstants.getFinalKey(String.format("%s:%d", queue, ServerConfig.serverId));
    }

    public static String getQueueKey(String queue, String serverId) {
        return CacheConstants.getFinalKey(String.format("%s:%s", queue, serverId));

    }
}
