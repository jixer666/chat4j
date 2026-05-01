package com.abc.chat4j.platform.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageReadDTO {

    public static final Integer READ_MESSAGE = 1;
    public static final Integer READ_CONVERSATION = 2;

    // 读取类型
    private Integer type;

    // 读取消息ID列表
    private List<Long> msgIdList;

    // 读取的会话ID
    private Long conversationId;

    // 后端设置
    private Long userId;

}
