package com.abc.chat4j.im.domain.dto;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Data
public class ImSendInfo {

    /**
     * @see com.abc.chat4j.im.domain.enums.ImMessageTypeEnum
     */
    private Integer type;

    private Object data;

    private Long roomId;

    private Long tempMsgId;

    // 后端设置
    private Long userId;
    private Long msgId;


}
