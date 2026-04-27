package com.abc.chat4j.platform.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationQueryContext {

    private Long userId;

    private Long minConversationId;

}
