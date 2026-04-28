package com.abc.chat4j.platform.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatusEnum {

    PENDING(1, "待发送"),
    SENT(2, "已发送");

    private Integer status;
    private String desc;

}
