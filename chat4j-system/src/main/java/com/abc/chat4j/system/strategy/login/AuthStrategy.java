package com.abc.chat4j.system.strategy.login;

import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.domain.dto.LoginDTO;
import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.system.domain.dto.RegisterDTO;

public interface AuthStrategy {

    LoginUserDTO authenticate(LoginDTO loginDTO);

    User doRegister(RegisterDTO registerDTO);

}
