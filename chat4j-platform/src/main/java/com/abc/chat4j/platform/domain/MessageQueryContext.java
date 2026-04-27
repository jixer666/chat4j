package com.abc.chat4j.platform.domain;

import lombok.Data;

import java.util.Date;

@Data
public class MessageQueryContext {

    private Long msgId;

    private Long userId;

    private Date begin;

    private Date end;

}
