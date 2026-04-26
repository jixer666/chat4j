package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

@Data
public class RegisterDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码
     */
    private String passwordAgain;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 认证方式
     */
    private Integer authType;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String emailCode;

    /**
     * 邮箱唯一标识
     */
    private String emailUuid;

    public void checkAccountParams() {
        AssertUtils.isNotEmpty(username, "账户不能为空");
        AssertUtils.isNotEmpty(password, "密码不能为空");
        AssertUtils.isNotEmpty(passwordAgain, "确认密码不能为空");
        AssertUtils.isTrue(password.equalsIgnoreCase(passwordAgain), "两次输入密码不一致");
        AssertUtils.isNotEmpty(authType, "注册方式不能为空");
        AssertUtils.isNotEmpty(uuid, "验证码不能为空");
        AssertUtils.isNotEmpty(code, "验证码不能为空");
    }

    public void checkEmailParams() {
        AssertUtils.isNotEmpty(authType, "注册方式不能为空");
        AssertUtils.isNotEmpty(email, "邮箱不能为空");
        AssertUtils.isNotEmpty(emailCode, "验证码不能为空");
        AssertUtils.isNotEmpty(emailUuid, "验证码不能为空");
    }
}
