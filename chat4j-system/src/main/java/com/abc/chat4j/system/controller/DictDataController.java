package com.abc.chat4j.system.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.DictDataDTO;
import com.abc.chat4j.system.domain.vo.DictDataVO;
import com.abc.chat4j.system.service.DictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "字典数据接口")
@RestController
@RequestMapping("/system/dictData")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    @ApiOperation("查询字典数据分页")
    @GetMapping("/page")
    public ApiResult<PageResult> getDictDataPage(DictDataDTO dictDataDTO) {
        PageResult dictDataPages = dictDataService.getDictDataPageWithUiParam(dictDataDTO);

        return ApiResult.success(dictDataPages);
    }

    @ApiOperation("更新字典数据")
    @PutMapping
    public ApiResult<Void> updateDictData(@RequestBody DictDataDTO dictDataDTO) {
        dictDataService.updateDictData(dictDataDTO);

        return ApiResult.success();
    }

    @ApiOperation("新增字典数据")
    @PostMapping
    public ApiResult<Void> saveDictData(@RequestBody DictDataDTO dictDataDTO) {
        dictDataService.saveDictData(dictDataDTO);

        return ApiResult.success();
    }

    @ApiOperation("删除字典数据")
    @DeleteMapping
    public ApiResult<Void> deleteDictData(@RequestBody DictDataDTO dictDataDTO) {
        dictDataService.deleteDictData(dictDataDTO);

        return ApiResult.success();
    }

    @ApiOperation("查询字典数据分页")
    @GetMapping("/key/{dictKey}")
    public ApiResult<List<DictDataVO>> getDictDataByDictKey(@PathVariable String dictKey) {
        List<DictDataVO> dictDataVOList = dictDataService.getDictDataByDictKey(dictKey);

        return ApiResult.success(dictDataVOList);
    }


}
