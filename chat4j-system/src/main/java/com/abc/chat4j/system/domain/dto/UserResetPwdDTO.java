package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

@Data
public class UserResetPwdDTO {

    private Long userId;

    private String oldPassword;

    private String newPassword;

    public void adminCheckParams() {
        AssertUtils.isNotEmpty(this, "角色参数不能为空");
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(newPassword, "新密码参数不能为空");
    }

    public void commonCheckParams() {
        adminCheckParams();
        AssertUtils.isNotEmpty(oldPassword, "旧密码参数不能为空");
    }
}
