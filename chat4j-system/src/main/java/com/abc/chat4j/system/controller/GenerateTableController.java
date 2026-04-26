package com.abc.chat4j.system.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.GenerateTableDTO;
import com.abc.chat4j.system.domain.entity.GenerateTable;
import com.abc.chat4j.system.domain.vo.GenerateTablePreviewVO;
import com.abc.chat4j.system.service.GenerateTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "代码生成表接口")
@RestController
@RequestMapping("/system/generateTable")
public class GenerateTableController {

    @Autowired
    private GenerateTableService generateTableService;

    @ApiOperation("查询代码生成表分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getGenerateTablePage(GenerateTableDTO generateTableDTO) {
        PageResult generateTablePages = generateTableService.getGenerateTablePageWithUiParam(generateTableDTO);

        return ApiResult.success(generateTablePages);
    }

    @ApiOperation("更新代码生成表")
    @PutMapping
    public ApiResult<Void> updateGenerateTable(@RequestBody GenerateTableDTO generateTableDTO) {
        generateTableService.updateGenerateTable(generateTableDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增代码生成表")
    @PostMapping
    public ApiResult<Void> saveGenerateTable(@RequestBody GenerateTableDTO generateTableDTO) {
        generateTableService.saveGenerateTable(generateTableDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除代码生成表")
    @DeleteMapping
    public ApiResult<Void> deleteGenerateTable(@RequestBody GenerateTableDTO generateTableDTO) {
        generateTableService.deleteGenerateTable(generateTableDTO);

        return ApiResult.success();
    }

    @ApiOperation("查询代码生成表分页")
    @GetMapping("/db/page")
    public ApiResult<PageResult> getGenerateDbPage(GenerateTableDTO generateTableTableDTO) {
        PageResult generateTablePages = generateTableService.getGenerateDbPageWithUiParam(generateTableTableDTO);

        return ApiResult.success(generateTablePages);
    }

    @ApiOperation("导入表结构")
    @PostMapping("/importTable")
    public ApiResult<Void> importTable(@RequestBody GenerateTableDTO generateTableTableDTO) {
        generateTableService.importTable(generateTableTableDTO);

        return ApiResult.success();
    }

    @ApiOperation("预览生成代码")
    @PostMapping("/preview/{genTableId}")
    public ApiResult<GenerateTablePreviewVO> previewCode(@PathVariable Long genTableId) {
        GenerateTablePreviewVO previewVO = generateTableService.previewCode(genTableId);

        return ApiResult.success(previewVO);
    }

    @ApiOperation("生成代码")
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadCode(@RequestBody GenerateTableDTO generateTableDTO) {

        return generateTableService.downloadCode(generateTableDTO);
    }

    @ApiOperation("获取生成表信息")
    @GetMapping("/info/{genTableId}")
    public ApiResult<GenerateTable> getGenerateTableInfo(@PathVariable Long genTableId) {
        GenerateTable genTable = generateTableService.getGenerateTableByGenTableId(genTableId);

        return ApiResult.success(genTable);
    }

}
