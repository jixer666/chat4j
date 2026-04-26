package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.system.domain.enums.CaptchaEnum;
import lombok.Data;

@Data
public class CaptchaDTO {

    private Integer type = CaptchaEnum.CHAR.getType();


    // 生成验证码过程中的参数
    private String uuid;
    private String code;
    private String img;


    public boolean isMathType() {
        return type.equals(CaptchaEnum.MATH.getType());
    }

    public boolean isCharType() {
        return type.equals(CaptchaEnum.CHAR.getType());
    }

}
