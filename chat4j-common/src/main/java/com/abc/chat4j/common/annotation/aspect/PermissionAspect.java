package com.abc.chat4j.common.annotation.aspect;

import com.abc.chat4j.common.annotation.Permission;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.common.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author LiJunXi
 * @date 2025/12/12
 */
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(permission)")
    public Object  checkPermission(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        LoginUserDTO loginUser = SecurityUtils.getLoginUser();
        AssertUtils.isNotEmpty(loginUser, "无权限");

        Set<String> rolesSet = loginUser.getRoles();
        // 超级管理员直接放行
        if (rolesSet.contains(CommonConstants.SUPER_ADMIN)) {
            return joinPoint.proceed();
        }

        Boolean hasPermission = false;
        Boolean hashRole = false;
        // 判断角色
        if (permission.roles() == null || permission.roles().length == 0) {
            hashRole = true;
        }
        for (String role : permission.roles()) {
            if (rolesSet.contains(role)) {
                hashRole = true;
            }
        }
        // 判断权限
        if (StringUtils.isEmpty(permission.value())) {
            hasPermission = true;
        }
        for (String perms : loginUser.getPermissions()) {
            if (perms.equals(permission.value())) {
                hasPermission = true;
            }
        }
        AssertUtils.isTrue(hashRole && hasPermission, "无权限");

        return joinPoint.proceed();
    }


}
