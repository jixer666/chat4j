package com.abc.chat4j.system.security;

import cn.hutool.json.JSONUtil;
import com.abc.chat4j.common.constant.HttpStatus;
import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.ServletUtils;
import com.abc.chat4j.common.util.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSONUtil.toJsonStr(ApiResult.fail(HttpStatus.UNAUTHORIZED, msg)));
    }
}
