package com.abc.chat4j.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.core.service.BaseServiceImpl;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.system.convert.UserConvert;
import com.abc.chat4j.system.domain.dto.UserDTO;
import com.abc.chat4j.system.domain.dto.UserResetPwdDTO;
import com.abc.chat4j.system.domain.vo.UserRoleVO;
import com.abc.chat4j.system.domain.vo.UserVO;
import com.abc.chat4j.system.mapper.UserMapper;
import com.abc.chat4j.system.service.RoleService;
import com.abc.chat4j.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public User getUserById(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        return userMapper.selectById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        AssertUtils.isNotEmpty(username, "账号不能为空");
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void saveUser(User user) {
        AssertUtils.isNotEmpty(user, "用户参数不能为空");
        userMapper.insert(user);
    }

    @Override
    public UserVO getUserInfoWithUiParam(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        User user = getUserById(userId);
        List<String> roleKeys = roleService.getRoleKeysByUserId(userId);

        return UserConvert.convertToUserVO(user, roleKeys);
    }

    @Override
    public PageResult getUserPageWithUiParam(UserDTO userDTO) {
        startPage();
        List<User> users = userMapper.selectUserList(userDTO);
        List<UserVO> userVOList =pageList2CustomList(users, (List<User> list) -> {
            return BeanUtil.copyToList(list, UserVO.class);
        });

        return buildPageResult(userVOList);
    }

    @Override
    public UserRoleVO getUserRole(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        List<?> roles = roleService.getRolePageWithUiParam(null).getList();
        List<Long> roleIds = roleService.getRoleIdsByUserId(userId);

        return UserConvert.buildUserRoleVO(roles, roleIds);
    }

    @Override
    @Transactional
    public void saveUserRole(UserDTO userDTO) {
        userDTO.checkSaveUserRoleParams();
        AssertUtils.isNotEmpty(getById(userDTO.getUserId()), "用户不存在");
        deleteUserAllRoleByUserId(userDTO.getUserId());
        saveUserRole(userDTO.getUserId(), userDTO.getRoleIds());
    }

    private void saveUserRole(Long userId, List<Long> roleIds) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(roleIds), "角色ID列表不能为空");

        roleService.saveUserRole(userId, roleIds);
    }

    private void deleteUserAllRoleByUserId(Long userId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");

        roleService.deleteRoleByUserId(userId);
    }

    @Override
    public void resetPassword(UserResetPwdDTO userResetPwdDTO) {
        userResetPwdDTO.adminCheckParams();

        User user = getUserById(userResetPwdDTO.getUserId());
        AssertUtils.isNotEmpty(user, "用户不存在");

        if (!roleService.isAdmin(SecurityUtils.getUserId())) {
            userResetPwdDTO.commonCheckParams();
            AssertUtils.isNotEmpty(user.getUserId().equals(SecurityUtils.getUserId()), "无权限");
            AssertUtils.isTrue(SecurityUtils.matchesPassword(userResetPwdDTO.getOldPassword(), user.getPassword()), "旧密码错误");
        }

        user.setPassword(new BCryptPasswordEncoder().encode(userResetPwdDTO.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        userDTO.checkUpdateUserParams();
        User user = userMapper.selectById(userDTO.getUserId());
        AssertUtils.isNotEmpty(user, "用户不存在");
        BeanUtils.copyProperties(userDTO, user);
        userMapper.updateById(user);
    }

    @Override
    public User getUserByEmail(String email) {
        AssertUtils.isNotEmpty(email, "邮箱不能为空");
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }
}
