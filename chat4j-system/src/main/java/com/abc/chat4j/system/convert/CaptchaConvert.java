package com.abc.chat4j.system.convert;

import com.abc.chat4j.system.domain.dto.CaptchaDTO;
import com.abc.chat4j.system.domain.vo.CaptchaVO;

public class CaptchaConvert {
    public static CaptchaVO convertCaptchaVOByCaptchaDTO(CaptchaDTO captchaDTO) {
        return CaptchaVO.builder()
                .img(captchaDTO.getImg())
                .uuid(captchaDTO.getUuid())
                .build();
    }
}
