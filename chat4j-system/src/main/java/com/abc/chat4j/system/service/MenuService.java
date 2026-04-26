package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.MenuDTO;
import com.abc.chat4j.system.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> getMenusByUserId(Long userId);

    PageResult getMenuPageWithUiParam(MenuDTO menuDTO);

    void updateMenu(MenuDTO menuDTO);

    void saveMenu(MenuDTO menuDTO);

    List<Long> getMenuIdsByRoleId(Long roleId);

    void deleteMenu(MenuDTO menuDTO);

    List<Menu> getMenusByMenuType(Integer type);
}
