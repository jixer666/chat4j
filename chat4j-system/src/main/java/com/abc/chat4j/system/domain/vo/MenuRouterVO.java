package com.abc.chat4j.system.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuRouterVO {

    private String name;

    private String component;

    private String path;

    private Boolean hidden;

    private Boolean alwaysShow;

    private String redirect;

    private List<MenuRouterVO> children;

    public MenuRouterMetaVo meta;

    private Boolean front;

}
