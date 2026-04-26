package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.constant.MenuConstants;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.system.domain.dto.MenuDTO;
import com.abc.chat4j.system.domain.entity.Menu;
import com.abc.chat4j.system.domain.vo.MenuRouterMetaVo;
import com.abc.chat4j.system.domain.vo.MenuRouterVO;
import com.abc.chat4j.system.domain.vo.MenuVO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class MenuConvert {

    public static MenuRouterVO convertToMenuRouterVO(Menu menu, List<MenuRouterVO> children) {
        MenuRouterVO menuRouterVO = new MenuRouterVO();
        menuRouterVO.setName(getMenuRouterName(menu));
        menuRouterVO.setChildren(children);
        menuRouterVO.setPath(getMenuRouterPath(menu));
        menuRouterVO.setComponent(getMenuRouterComponent(menu));
        menuRouterVO.setMeta(buildMetaVo(menu));
        menuRouterVO.setHidden(menu.getHidden().equals(CommonConstants.NO));
        menuRouterVO.setFront(menu.isFront());
        if (CollectionUtils.isNotEmpty(children) && menu.isMuLu()) {
            menuRouterVO.setAlwaysShow(true);
            menuRouterVO.setRedirect(MenuConstants.NO_REDIRECT);
        }

        return menuRouterVO;
    }

    private static String getMenuRouterPath(Menu menu) {
        if (menu.isMuLu() || menu.isFront()) {
            return "/" + menu.getPath();
        }
        return menu.getPath();
    }

    private static String getMenuRouterName(Menu menu) {
        return StringUtils.capitalize(menu.getPath());
    }

    private static MenuRouterMetaVo buildMetaVo(Menu menu) {
        return MenuRouterMetaVo.builder()
                .title(menu.getMenuName())
                .icon(menu.getIcon())
                .build();
    }


    private static String getMenuRouterComponent(Menu menu) {
        if (StringUtils.isNotEmpty(menu.getComponent())) {
            return menu.getComponent();
        }

        return MenuConstants.LAYOUT;
    }

    public static MenuVO convertToMenuVO(Menu menu, List<MenuVO> children) {
        MenuVO menuVO = BeanUtil.copyProperties(menu, MenuVO.class);
        menuVO.setChildren(children);

        return menuVO;
    }

    public static Menu buildDefaultMenuByMenuDTO(MenuDTO menuDTO) {
        Menu menu = BeanUtil.copyProperties(menuDTO, Menu.class);
        menu.setMenuId(IdUtils.getId());
        menu.setUserId(SecurityUtils.getUserId());
        if (Objects.isNull(menu.getParentId())) {
            menu.setParentId(MenuConstants.DEFAULT_MENU_PARENT_ID);
        }
        if (Objects.isNull(menu.getHidden())) {
            menu.setHidden(CommonConstants.YES);
        }
        menu.setCommonParams();

        return menu;
    }
}
