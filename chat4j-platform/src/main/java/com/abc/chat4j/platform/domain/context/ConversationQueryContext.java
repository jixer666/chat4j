package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationQueryContext {

    private Long userId;

    private Date minUpdateTime;

}
