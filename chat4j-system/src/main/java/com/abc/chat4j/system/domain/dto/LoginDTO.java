package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 登录方式
     */
    private Integer authType;

    public void checkParams() {
        AssertUtils.isNotEmpty(username, "账户不能为空");
        AssertUtils.isNotEmpty(password, "密码不能为空");
        AssertUtils.isNotEmpty(authType, "登录方式不能为空");

    }
}
