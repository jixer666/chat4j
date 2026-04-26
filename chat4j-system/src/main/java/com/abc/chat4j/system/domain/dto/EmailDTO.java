package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EmailDTO {

    private String email;

    // 生成验证码过程中的参数
    private Integer emailType;
    private String emailUuid;
    private String emailCode;

    // 发送邮件设置参数
    private String from;
    private String to;
    private Map<String,String> detailsMap = new HashMap<>();

    public void checkSendParams() {
        AssertUtils.isNotEmpty(this, "参数不能为空");
        AssertUtils.isNotEmpty(email, "邮箱不能为空");
        AssertUtils.isNotEmpty(emailType, "邮箱类型不能为空");
    }
}
