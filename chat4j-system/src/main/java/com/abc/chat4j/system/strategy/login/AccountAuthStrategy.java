package com.abc.chat4j.system.strategy.login;

import com.abc.chat4j.common.domain.enums.CommonRoleEnum;
import com.abc.chat4j.system.security.context.SecurityAuthContext;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.convert.UserConvert;
import com.abc.chat4j.system.domain.dto.LoginDTO;
import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.system.domain.dto.RegisterDTO;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.service.EmailService;
import com.abc.chat4j.system.service.IndexService;
import com.abc.chat4j.system.service.RoleService;
import com.abc.chat4j.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AccountAuthStrategy implements AuthStrategy {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Override
    public LoginUserDTO authenticate(LoginDTO loginDTO) {
        preLoginCheck(loginDTO);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        SecurityAuthContext.setContext(authToken);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityAuthContext.removeContext();

        return (LoginUserDTO) authentication.getPrincipal();
    }

    public void preLoginCheck(LoginDTO loginDTO) {
        loginDTO.checkParams();
    }

    @Override
    public User doRegister(RegisterDTO registerDTO) {
        preRegisterCheck(registerDTO);
        User user = UserConvert.convertToUserByRegisterDTO(registerDTO);
        userService.saveUser(user);
        roleService.saveUserRoleByRoleKeys(user.getUserId(), Collections.singletonList(CommonRoleEnum.COMMON_USER.getRoleKey()));
        afterRegister(registerDTO);

        return user;
    }

    public void preRegisterCheck(RegisterDTO registerDTO) {
        registerDTO.checkAccountParams();
        User user = userService.getUserByUsername(registerDTO.getUsername());
        AssertUtils.isEmpty(user, "用户已存在");
        user = userService.getUserByEmail(registerDTO.getEmail());
        AssertUtils.isEmpty(user, "邮箱已被绑定");
        Boolean checkEmailCode = indexService.checkEmailCode(registerDTO.getEmailUuid(), registerDTO.getEmailCode());
        AssertUtils.isTrue(checkEmailCode, "邮箱验证码错误");
    }

    private void afterRegister(RegisterDTO registerDTO) {
        emailService.invalidEmailCode(registerDTO.getEmailUuid());
    }
}
