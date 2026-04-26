package com.abc.chat4j.system.strategy.login;

import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.domain.dto.LoginDTO;
import com.abc.chat4j.system.domain.dto.RegisterDTO;
import com.abc.chat4j.system.service.IndexService;
import com.abc.chat4j.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthStrategy implements AuthStrategy {

    @Autowired
    private UserService userService;

    @Autowired
    private IndexService indexService;

    @Override
    public LoginUserDTO authenticate(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public User doRegister(RegisterDTO registerDTO) {
        preRegisterCheck(registerDTO);

        // todo 邮箱注册
        return null;
    }

    public void preRegisterCheck(RegisterDTO registerDTO) {
        registerDTO.checkEmailParams();
        User user = userService.getUserByEmail(registerDTO.getEmail());
        AssertUtils.isEmpty(user, "邮箱已被绑定");

        Boolean checkEmailCode = indexService.checkEmailCode(registerDTO.getEmailUuid(), registerDTO.getEmailCode());
        AssertUtils.isTrue(checkEmailCode, "邮箱验证码错误");
    }
}
