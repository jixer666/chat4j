package com.abc.chat4j.system.service;

import com.abc.chat4j.system.domain.dto.EmailDTO;
import com.abc.chat4j.system.domain.vo.EmailVO;

public interface EmailService {

    EmailVO sendEmail(EmailDTO emailDTO);

    void invalidEmailCode(String emailUuid);
}
