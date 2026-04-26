package com.abc.chat4j.system.service.impl;

import com.abc.chat4j.system.domain.entity.Menu;
import com.abc.chat4j.system.security.context.SecurityAuthContext;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.service.MenuService;
import com.abc.chat4j.system.service.RoleService;
import com.abc.chat4j.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        AssertUtils.isNotEmpty(user, "用户不存在");
        AssertUtils.isTrue(SecurityUtils.matchesPassword(SecurityAuthContext.getContext().getCredentials().toString(),
                user.getPassword()), "账号或者密码错误");

        List<String> roles = roleService.getRoleKeysByUserId(user.getUserId());
        Set<String> perms = menuService.getMenusByUserId(user.getUserId())
                .stream()
                .map(Menu::getPerms)
                .filter(item -> !Objects.isNull(item))
                .collect(Collectors.toSet());

        return new LoginUserDTO(user, perms, new HashSet<>(roles));
    }
}
