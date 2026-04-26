package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.RoleDTO;
import com.abc.chat4j.system.domain.entity.Role;
import com.abc.chat4j.system.domain.vo.RoleMenuTreeVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<String> getRoleKeysByUserId(Long userId);

    List<Long> getRoleIdsByUserId(Long userId);

    PageResult getRolePageWithUiParam(RoleDTO roleDTO);

    void updateRole(RoleDTO roleDTO);

    void saveRole(RoleDTO roleDTO);

    RoleMenuTreeVO getRoleMenuTree(Long roleId);

    void saveRoleMenu(RoleDTO roleDTO);

    void deleteRole(RoleDTO roleDTO);

    List<Long> getRoleIdsByMenuId(Long menuId);

    void deleteRoleByUserId(Long userId);

    void saveUserRole(Long userId, List<Long> roleIds);

    Boolean isAdmin(Long userId);

    void saveUserRoleByRoleKeys(Long userId, List<String> roleKeys);

    List<Role> getRoleListByRoleKeys(List<String> roleKeys);
}
