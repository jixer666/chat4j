package com.abc.chat4j.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    NORMAL(1),
    BAN(2);

    private Integer status;


}
