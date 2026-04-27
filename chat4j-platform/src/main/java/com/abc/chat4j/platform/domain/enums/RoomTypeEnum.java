package com.abc.chat4j.platform.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomTypeEnum {

    PRIVATE(1, "私聊"),
    GROUP(2, "群聊");

    private Integer type;
    private String desc;

}
