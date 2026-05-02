package com.abc.chat4j.platform.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Getter
@AllArgsConstructor
public enum FriendApplyStatusEnum {

    PENDING(1, "待处理"),
    ACCEPT(2, "同意"),
    REJECT(3, "拒绝");

    private Integer status;
    private String desc;

}
