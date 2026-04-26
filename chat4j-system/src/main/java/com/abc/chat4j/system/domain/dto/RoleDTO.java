package com.abc.chat4j.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private Long roleId;

    private String roleName;

    private String roleKey;

    // 用于分配菜单权限
    private List<Long> menuIds;

    // 用于删除角色
    private List<Long> roleIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "角色参数不能为空");
        AssertUtils.isNotEmpty(roleId, "角色ID不能为空");

    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "角色参数不能为空");
        AssertUtils.isNotEmpty(roleName, "角色名称不能为空");
        AssertUtils.isNotEmpty(roleKey, "角色字符不能为空");
    }

    public void checkSaveRoleMenuParams() {
        AssertUtils.isNotEmpty(this, "角色参数不能为空");
        AssertUtils.isNotEmpty(roleId, "角色ID不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "角色参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(roleIds), "角色ID列表不能为空");
    }
}
