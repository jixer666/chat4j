package com.abc.chat4j.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    ACCOUNT(1, "accountAuthStrategy"),
    EMAIL(2, "emailAuthStrategy");
//    PHONE(2, "phoneLoginStrategy"),

    private final Integer type;
    private final String loginClass;

    public static Integer getTypeByClass(String loginClass) {
        return Arrays.stream(LoginTypeEnum.values()).filter(item -> item.getLoginClass().equals(loginClass)).findFirst().map(LoginTypeEnum::getType).orElse(null);
    }

    public static Integer getClassByType(Integer loginType) {
        return Arrays.stream(LoginTypeEnum.values()).filter(item -> item.getType().equals(loginType)).findFirst().map(LoginTypeEnum::getType).orElse(null);
    }
}
