package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.Date;

@Data
public class MessageQueryContext {

    private Date minUpdateTime;

    private Long userId;

}
