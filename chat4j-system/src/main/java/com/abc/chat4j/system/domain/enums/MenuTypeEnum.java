package com.abc.chat4j.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuTypeEnum {
    MU_LU(1, "目录"),
    CAI_DAN(2, "菜单"),
    AN_NIU(3, "按钮"),
    FRONT(4, "前台");

    private Integer type;
    private String desc;

}
