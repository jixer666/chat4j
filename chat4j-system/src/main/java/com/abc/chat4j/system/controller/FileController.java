package com.abc.chat4j.system.controller;

import com.abc.chat4j.common.annotation.Permission;
import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.FileDTO;
import com.abc.chat4j.system.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文件控制器
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Api(tags = "文件接口")
@RestController
@RequestMapping("/system/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Permission("system:file:list")
    @ApiOperation("查询文件分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getFilePage(FileDTO fileDTO) {
        PageResult filePages = fileService.getFilePageWithUiParam(fileDTO);

        return ApiResult.success(filePages);
    }

    @Permission("system:file:edit")
    @ApiOperation("更新文件")
    @PutMapping
    public ApiResult<Void> updateFile(@RequestBody FileDTO fileDTO) {
        fileService.updateFile(fileDTO);

        return ApiResult.success();
    }

    @Permission("system:file:add")
    @ApiOperation("新增文件")
    @PostMapping
    public ApiResult<Void> saveFile(@RequestBody FileDTO fileDTO) {
        fileService.saveFile(fileDTO);

        return ApiResult.success();
    }

    @Permission("system:file:delete")
    @ApiOperation("删除文件")
    @DeleteMapping
    public ApiResult<Void> deleteFile(@RequestBody FileDTO fileDTO) {
        fileService.deleteFile(fileDTO);

        return ApiResult.success();
    }

}
