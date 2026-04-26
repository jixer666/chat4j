package com.abc.chat4j.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EmailTypeEnum {

    REGISTER(1, "registerEmailStrategy");

    private final Integer type;
    private final String emailClass;

    public static Integer getTypeByClass(String emailClass) {
        return Arrays.stream(EmailTypeEnum.values()).filter(item -> item.getEmailClass().equals(emailClass)).findFirst().map(EmailTypeEnum::getType).orElse(null);
    }

    public static Integer getClassByType(Integer loginType) {
        return Arrays.stream(EmailTypeEnum.values()).filter(item -> item.getType().equals(loginType)).findFirst().map(EmailTypeEnum::getType).orElse(null);
    }
}
