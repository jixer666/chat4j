package com.abc.chat4j.system.security;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.ServletUtils;
import com.abc.chat4j.system.service.TokenService;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenService.getToken(request);
        tokenService.invalidToken(token);
        ServletUtils.renderString(response, JSON.toJSONString(ApiResult.success("退出成功")));
    }
}
