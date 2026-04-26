package com.abc.chat4j.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.RedisUtils;
import com.abc.chat4j.system.constant.EmailConstants;
import com.abc.chat4j.system.domain.dto.EmailDTO;
import com.abc.chat4j.system.domain.vo.EmailVO;
import com.abc.chat4j.system.factory.EmailStrategyFactory;
import com.abc.chat4j.system.service.EmailService;
import com.abc.chat4j.system.strategy.email.EmailStrategy;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public EmailVO sendEmail(EmailDTO emailDTO) {
        emailDTO.checkSendParams();
        EmailStrategy emailStrategy = EmailStrategyFactory.getEmailStrategy(emailDTO.getEmailType());
        fillEmailParams(emailDTO);

        return emailStrategy.sendEmail(emailDTO);
    }

    private static void fillEmailParams(EmailDTO emailDTO) {
        emailDTO.setEmailUuid(RandomUtil.randomString(8));
        emailDTO.setFrom(EmailConstants.FROM_EMAIL);
        emailDTO.setTo(emailDTO.getEmail());
    }

    @Override
    public void invalidEmailCode(String emailUuid) {
        AssertUtils.isNotEmpty(emailUuid, "邮箱验证码不能为空");

        RedisUtils.del(CacheConstants.getFinalKey(CacheConstants.EMAIL_UUID, emailUuid));
    }
}
