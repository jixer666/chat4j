package com.abc.chat4j.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.core.service.BaseServiceImpl;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.convert.GenerateTableColumnConvert;
import com.abc.chat4j.system.domain.dto.GenerateTableColumnDTO;
import com.abc.chat4j.system.domain.entity.GenerateTableColumn;
import com.abc.chat4j.system.domain.vo.GenerateTableColumnVO;
import com.abc.chat4j.system.mapper.GenerateTableColumnMapper;
import com.abc.chat4j.system.service.GenerateTableColumnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerateTableColumnServiceImpl extends BaseServiceImpl<GenerateTableColumnMapper, GenerateTableColumn> implements GenerateTableColumnService {

    @Autowired
    private GenerateTableColumnMapper generateTableColumnMapper;

    @Override
    public PageResult getGenerateTableColumnPageWithUiParam(GenerateTableColumnDTO generateTableColumnDTO) {
        startPage();
        List<GenerateTableColumn> generateTableColumns = generateTableColumnMapper.selectGenerateTableColumnList(generateTableColumnDTO);
        List<GenerateTableColumnVO> generateTableColumnVOList = pageList2CustomList(generateTableColumns, (List<GenerateTableColumn> list) -> {
            return BeanUtil.copyToList(list, GenerateTableColumnVO.class);
        });

        return buildPageResult(generateTableColumnVOList);
    }

    @Override
    public void updateGenerateTableColumn(GenerateTableColumnDTO generateTableColumnDTO) {
        generateTableColumnDTO.checkUpdateParams();
        GenerateTableColumn generateTableColumn = generateTableColumnMapper.selectById(generateTableColumnDTO.getGenTableColumnId());
        AssertUtils.isNotEmpty(generateTableColumn, "菜单不存在");
        BeanUtils.copyProperties(generateTableColumnDTO, generateTableColumn);
        generateTableColumnMapper.updateById(generateTableColumn);
    }

    @Override
    public void saveGenerateTableColumn(GenerateTableColumnDTO generateTableColumnDTO) {
        generateTableColumnDTO.checkSaveParams();
        GenerateTableColumn generateTableColumn = GenerateTableColumnConvert.buildDefaultGenerateTableColumnByGenerateTableColumnDTO(generateTableColumnDTO);
        generateTableColumnMapper.insert(generateTableColumn);
    }

    @Override
    public void deleteGenerateTableColumn(GenerateTableColumnDTO generateTableColumnDTO) {
        generateTableColumnDTO.checkDeleteParams();

        generateTableColumnMapper.deleteBatchIds(generateTableColumnDTO.getGenTableColumnIds());
    }

    @Override
    public void importTableColumn(String tableName, Long generateTableId) {
        AssertUtils.isNotEmpty(tableName, "表名不能为空");
        AssertUtils.isNotEmpty(generateTableId, "生成表ID不能为空");

        List<GenerateTableColumn> generateTableColumns = generateTableColumnMapper.selectDbTableColumnsByName(tableName);
        List<GenerateTableColumn> tableColumns = generateTableColumns.stream().map(item -> {
            item.setGenTableId(generateTableId);
            GenerateTableColumnConvert.initDefaultGenerateTableColumn(item);
            return item;
        }).collect(Collectors.toList());

        saveBatch(tableColumns);
    }

    @Override
    public void updateGenerateTableColumns(List<GenerateTableColumn> tableColumns) {
        if (CollUtil.isEmpty(tableColumns)) {
            return;
        }
        updateBatchById(tableColumns);
    }

    @Override
    public void deleteGenerateTableColumnByGenTableIds(List<Long> genTableIds) {
        if (CollUtil.isEmpty(genTableIds)) {
            return;
        }
        generateTableColumnMapper.deleteGenerateTableColumnByGenTableIds(genTableIds);
    }
}
