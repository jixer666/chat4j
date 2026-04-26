package com.abc.chat4j.system.convert;

import com.abc.chat4j.system.constant.EmailConstants;
import com.abc.chat4j.system.domain.dto.EmailDTO;
import com.abc.chat4j.system.domain.vo.EmailVO;

public class EmailConvert {

    public static EmailVO buildEmailVoByEmailDTO(EmailDTO emailDTO) {
        EmailVO emailVO = new EmailVO();
        emailVO.setEmailUuid(emailDTO.getEmailUuid());
        emailVO.setEmail(emailDTO.getDetailsMap().get(EmailConstants.EMAIL_CODE));

        return emailVO;
    }
}
