package com.abc.chat4j.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CaptchaEnum {

    MATH(1, "数字验证码"),
    CHAR(2, "字符验证码");

    private Integer type;
    private String desc;

}
