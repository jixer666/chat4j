package com.abc.chat4j.platform.domain.vo;

import lombok.Data;

@Data
public class RoomInfoVO {

    private Long roomId;

    private Integer type;

    // 单聊/群聊具体的数据
    private Object data;
}
