package com.abc.chat4j.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Getter
@AllArgsConstructor
public enum DeviceEnum {
    WEB(1, "Web端"),
    PC(2, "PC端"),
    APP(3, "App端"),
    ;

    public Integer type;
    private String desc;

    public static DeviceEnum typeOf(Integer type) {
        return Arrays.stream(DeviceEnum.values())
                .filter(deviceEnum -> Objects.equals(deviceEnum.type, type))
                .findFirst().orElse(null);
    }

}
