package com.abc.chat4j.platform.domain.dto;

import lombok.Data;

@Data
public class FriendApplyDTO {

    public static final Integer ACCEPT = 1;
    public static final Integer REJECT = 2;

    private Long friendApplyId;

    private Long friendId;

    private String remark;

    // 1：同意 2：拒绝
    private Integer operationType;

    // 后端设置
    private Long userId;

}
