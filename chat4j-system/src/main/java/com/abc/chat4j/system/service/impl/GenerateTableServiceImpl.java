package com.abc.chat4j.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.core.generate.VelocityInitializer;
import com.abc.chat4j.common.core.service.BaseServiceImpl;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.util.GenerateUtils;
import com.abc.chat4j.system.convert.GenerateTableConvert;
import com.abc.chat4j.system.domain.dto.GenerateTableDTO;
import com.abc.chat4j.system.domain.entity.GenerateTable;
import com.abc.chat4j.system.domain.vo.GenerateTablePreviewVO;
import com.abc.chat4j.system.domain.vo.GenerateTableVO;
import com.abc.chat4j.system.mapper.GenerateTableMapper;
import com.abc.chat4j.system.service.GenerateTableColumnService;
import com.abc.chat4j.system.service.GenerateTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class GenerateTableServiceImpl extends BaseServiceImpl<GenerateTableMapper, GenerateTable> implements GenerateTableService {

    @Autowired
    private GenerateTableMapper generateTableMapper;

    @Autowired
    private GenerateTableColumnService generateTableColumnService;

    @Override
    public PageResult getGenerateTablePageWithUiParam(GenerateTableDTO generateTableDTO) {
        startPage();
        List<GenerateTable> generateTables = generateTableMapper.selectGenerateTableList(generateTableDTO);
        List<GenerateTableVO> generateTableVOList = pageList2CustomList(generateTables, (List<GenerateTable> list) -> {
            return BeanUtil.copyToList(list, GenerateTableVO.class);
        });

        return buildPageResult(generateTableVOList);
    }

    @Override
    @Transactional
    public void updateGenerateTable(GenerateTableDTO generateTableDTO) {
        generateTableDTO.checkUpdateParams();
        GenerateTable generateTable = generateTableMapper.selectById(generateTableDTO.getGenTableId());
        AssertUtils.isNotEmpty(generateTable, "菜单不存在");
        BeanUtils.copyProperties(generateTableDTO, generateTable);
        generateTableMapper.updateById(generateTable);
        generateTableColumnService.updateGenerateTableColumns(generateTableDTO.getTableColumns());
    }

    @Override
    public void saveGenerateTable(GenerateTableDTO generateTableDTO) {
        generateTableDTO.checkSaveParams();
        GenerateTable generateTable = GenerateTableConvert.buildDefaultGenerateTableByGenerateTableDTO(generateTableDTO);
        generateTableMapper.insert(generateTable);
    }

    @Override
    @Transactional
    public void deleteGenerateTable(GenerateTableDTO generateTableDTO) {
        generateTableDTO.checkDeleteParams();

        generateTableMapper.deleteBatchIds(generateTableDTO.getGenTableIds());
        generateTableColumnService.deleteGenerateTableColumnByGenTableIds(generateTableDTO.getGenTableIds());
    }


    @Override
    public PageResult getGenerateDbPageWithUiParam(GenerateTableDTO generateTableTableDTO) {
        startPage();
        List<GenerateTable> generateTables = generateTableMapper.selectDbTableList(generateTableTableDTO);
        List<GenerateTableVO> generateTableVOList = pageList2CustomList(generateTables, (List<GenerateTable> list) -> {
            return BeanUtil.copyToList(list, GenerateTableVO.class);
        });

        return buildPageResult(generateTableVOList);
    }

    @Override
    @Transactional
    public void importTable(GenerateTableDTO generateTableTableDTO) {
        generateTableTableDTO.checkImportParams();
        List<GenerateTable> importTables = generateTableMapper.selectDbTableListByNames(generateTableTableDTO.getTableNames());
        importTablesAndColumn(importTables);
    }

    private void importTablesAndColumn(List<GenerateTable> importTables) {
        if (CollUtil.isEmpty(importTables)) {
            return;
        }
        for (GenerateTable generateTable : importTables) {
            GenerateTableConvert.initDefaultGenerateTable(generateTable);
            generateTableMapper.insert(generateTable);
            generateTableColumnService.importTableColumn(generateTable.getTableName(), generateTable.getGenTableId());
        }

    }

    @Override
    public GenerateTablePreviewVO previewCode(Long genTableId) {
        AssertUtils.isNotEmpty(genTableId, "生成代码表ID不能为空");
        GenerateTable table = getGenerateTableByGenTableId(genTableId);
        AssertUtils.isNotEmpty(table, "生成代码表不存在");

        VelocityInitializer.initVelocity();
        VelocityContext context = GenerateTableConvert.buildVelocityContextByGenerateTable(table);

        GenerateTablePreviewVO previewVO = new GenerateTablePreviewVO();
        for (String template : GenerateUtils.getTemplateList()) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CommonConstants.UTF8);
            tpl.merge(context, sw);
            previewVO.put(template, sw.toString());
        }

        return previewVO;
    }

    @Override
    public GenerateTable getGenerateTableByGenTableId(Long genTableId) {
        AssertUtils.isNotEmpty(genTableId, "生成代码表ID不能为空");

        return generateTableMapper.selectGenerateTableByGenTableId(genTableId);
    }

    @Override
    public ResponseEntity<byte[]> downloadCode(GenerateTableDTO generateTableDTO) {
        AssertUtils.isTrue(CollUtil.isNotEmpty(generateTableDTO.getGenTableIds()), "生成代码表ID列表为空");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8);

        List<GenerateTable> tables = generateTableMapper.selectGenerateTableByGenTableIds(generateTableDTO.getGenTableIds());
        for (GenerateTable generateTable : tables) {
            generateCode(generateTable, zipOutputStream);
        }

        try {
            zipOutputStream.finish();
        } catch (IOException e) {
            log.error("关闭输出流出错：{}", e.getMessage(), e);
            throw new GlobalException("关闭输出流出错");
        }
        return download(outputStream.toByteArray(), GenerateUtils.getZipFilename(tables));
    }

    private void generateCode(GenerateTable table, ZipOutputStream zipOutputStream) {
        VelocityInitializer.initVelocity();
        VelocityContext context = GenerateTableConvert.buildVelocityContextByGenerateTable(table);

        for (String template : GenerateUtils.getTemplateList()) {
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CommonConstants.UTF8);
            tpl.merge(context, sw);
            try {
                zipOutputStream.putNextEntry(new ZipEntry(GenerateUtils.getGenerateFileName(template, table)));
                IOUtils.write(sw.toString(), zipOutputStream, CommonConstants.UTF8);
                IOUtils.closeQuietly(sw);
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            }
            catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
                throw new GlobalException("渲染模板失败");
            }
        }
    }
}
