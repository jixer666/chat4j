package com.abc.chat4j.system.controller;

import com.abc.chat4j.common.annotation.Permission;
import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.MenuDTO;
import com.abc.chat4j.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "菜单接口")
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Permission("system:menu:list")
    @ApiOperation("查询菜单分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getMenuPage(MenuDTO menuDTO) {
        PageResult menuPages = menuService.getMenuPageWithUiParam(menuDTO);

        return ApiResult.success(menuPages);
    }

    @Permission("system:menu:edit")
    @ApiOperation("更新菜单")
    @PutMapping
    public ApiResult<Void> updateMenu(@RequestBody MenuDTO menuDTO) {
        menuService.updateMenu(menuDTO);

        return ApiResult.success();
    }

    @Permission("system:menu:add")
    @ApiOperation("新增菜单")
    @PostMapping
    public ApiResult<Void> saveMenu(@RequestBody MenuDTO menuDTO) {
        menuService.saveMenu(menuDTO);

        return ApiResult.success();
    }

    @Permission("system:menu:delete")
    @ApiOperation("删除菜单")
    @DeleteMapping
    public ApiResult<Void> deleteMenu(@RequestBody MenuDTO menuDTO) {
        menuService.deleteMenu(menuDTO);

        return ApiResult.success();
    }


}
