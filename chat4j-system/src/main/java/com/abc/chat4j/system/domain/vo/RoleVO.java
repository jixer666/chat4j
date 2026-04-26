package com.abc.chat4j.system.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RoleVO {

    private Long roleId;

    private String roleName;

    private String roleKey;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
