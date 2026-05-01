package com.abc.chat4j.platform.domain.dto;

import lombok.Data;

@Data
public class FriendApplyDTO {

    private Long friendId;

    private String remark;

    // 后端设置
    private Long userId;

}
