package com.abc.chat4j.system.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("tb_role")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {

    @TableId
    private Long roleId;

    private String roleName;

    private String roleKey;

    private Long userId;

}
