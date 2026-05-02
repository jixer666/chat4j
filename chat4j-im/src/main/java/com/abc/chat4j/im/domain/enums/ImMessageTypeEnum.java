package com.abc.chat4j.im.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Getter
@AllArgsConstructor
public enum ImMessageTypeEnum {

    AUTHORIZATION(1, "认证"),
    HEART(2, "心跳"),
    TEXT(10, "文本"),
    FRIEND_APPLY(100, "好友申请"),


    ERROR(9999, "错误信息"),
    ;


    private Integer type;
    private String desc;

    public static ImMessageTypeEnum typeOf(Integer type) {
        for (ImMessageTypeEnum imMessageTypeEnum : ImMessageTypeEnum.values()) {
            if (imMessageTypeEnum.getType().equals(type)) {
                return imMessageTypeEnum;
            }
        }
        return null;
    }


}
