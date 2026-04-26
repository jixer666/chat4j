package com.abc.chat4j.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonRoleEnum {

    SUPER_USER("super_user"),
    COMMON_USER("common_user");

    private String roleKey;

}
