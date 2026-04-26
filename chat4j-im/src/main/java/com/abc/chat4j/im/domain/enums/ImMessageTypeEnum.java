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
    TEXT(10, "文本"),

    ;


    private Integer type;
    private String desc;


}
