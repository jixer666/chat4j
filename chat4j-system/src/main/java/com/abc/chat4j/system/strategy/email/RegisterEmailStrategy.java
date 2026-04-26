package com.abc.chat4j.system.strategy.email;

import cn.hutool.core.util.RandomUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.RedisUtils;
import com.abc.chat4j.system.constant.EmailConstants;
import com.abc.chat4j.system.convert.EmailConvert;
import com.abc.chat4j.system.domain.dto.EmailDTO;
import com.abc.chat4j.system.domain.vo.EmailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RegisterEmailStrategy implements EmailStrategy {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public EmailVO sendEmail(EmailDTO emailDTO) {
        saveRegisterCode(emailDTO);

        try {
            doSend(mailSender, emailDTO);
        } catch (Exception e) {
            log.error("发送注册邮件出错，{}", e.getMessage(), e);
            throw new GlobalException("发送注册邮件出错");
        }

        return EmailConvert.buildEmailVoByEmailDTO(emailDTO);
    }

    private void saveRegisterCode(EmailDTO emailDTO) {
        String emailCacheKey = CacheConstants.getFinalKey(CacheConstants.EMAIL_UUID, emailDTO.getEmailUuid());
        String emailCode = RandomUtil.randomNumbers(6);

        emailDTO.getDetailsMap().put(EmailConstants.EMAIL_CODE, emailCode);

        RedisUtils.set(emailCacheKey, emailCode, CacheConstants.EMAIL_UUID_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public String getTitle(Map<String, String> params) {
        return EmailConstants.REGISTER_TITLE;
    }

    @Override
    public String getContent(Map<String, String> params) {
        return String.format(EmailConstants.REGISTER_CONTENT, params.get(EmailConstants.EMAIL_CODE));
    }
}
