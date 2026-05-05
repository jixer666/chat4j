package com.abc.chat4j.platform.domain.dto;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/5/5
 */
@Data
public class MessageReadCountDTO {

    private Long msgId;

    private Integer readCount;
}
