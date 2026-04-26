package com.abc.chat4j.system.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuVO {

    private Long menuId;

    private Long parentId;

    private String menuName;

    private String icon;

    private String path;

    private String component;

    private Integer orderNum;

    private Date createTime;

    private Integer menuType;

    private Integer hidden;

    private Integer status;

    private List<MenuVO> children;

    private String perms;

}
