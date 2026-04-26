package com.abc.chat4j.system.mapper;

import com.abc.chat4j.system.domain.dto.RoleDTO;
import com.abc.chat4j.system.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeysByUserId(Long userId);

    List<Long> selectRoleIdsByUserId(Long userId);

    List<Role> selectRoleList(RoleDTO roleDTO);

    void deleteRoleMenuByRoleId(Long roleId);

    void saveRoleMenu(@Param("roleId") Long roleId, @Param("list") List<Long> menuIds);

    List<Long> selectRoleIdsByMenuId(Long menuId);

    void deleteRoleByUserId(Long userId);

    void saveUserRole(@Param("userId") Long userId, @Param("list") List<Long> roleIds);

    List<Role> selectRoleListByRoleKeys(@Param("list") List<String> roleKeys);
}
