package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.constant.GeneratorConstants;
import com.abc.chat4j.common.util.DateUtils;
import com.abc.chat4j.system.util.GenerateUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.system.domain.dto.GenerateTableDTO;
import com.abc.chat4j.system.domain.entity.GenerateTable;
import com.abc.chat4j.system.domain.entity.GenerateTableColumn;
import org.apache.velocity.VelocityContext;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateTableConvert {
    public static GenerateTable buildDefaultGenerateTableByGenerateTableDTO(GenerateTableDTO generateTableDTO) {
        GenerateTable generateTable = BeanUtil.copyProperties(generateTableDTO, GenerateTable.class);
        generateTable.setGenTableId(IdUtils.getId());
        generateTable.setCommonParams();

        return generateTable;
    }

    public static void initDefaultGenerateTable(GenerateTable generateTable) {
        generateTable.setPackageName(GeneratorConstants.PACKAGE_NAME);
        generateTable.setClassName(GenerateUtils.getClassName(generateTable.getTableName()));
        generateTable.setFunctionAuthor(GeneratorConstants.AUTHOR);
        generateTable.setModuleName(GenerateUtils.getModuleName(generateTable.getPackageName()));
        generateTable.setBusinessName(GenerateUtils.getBusinessName(generateTable.getClassName()));
        generateTable.setFunctionName(GenerateUtils.getFunctionName(generateTable.getTableComment()));
        generateTable.setUserId(IdUtils.getId());
        generateTable.setCommonParams();
    }

    public static VelocityContext buildVelocityContextByGenerateTable(GenerateTable genTable) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", genTable.getTableName());
        velocityContext.put("functionName", StringUtils.isNotEmpty(genTable.getFunctionName()) ? genTable.getFunctionName() : "【请填写功能名称】");
        velocityContext.put("ClassName", genTable.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(genTable.getClassName()));
        velocityContext.put("moduleName", genTable.getModuleName());
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.getBusinessName()));
        velocityContext.put("businessName", genTable.getBusinessName());
//        velocityContext.put("basePackage", getPackagePrefix(packageName));
        velocityContext.put("packageName", genTable.getPackageName());
        velocityContext.put("author", genTable.getFunctionAuthor());
        velocityContext.put("datetime", DateUtils.getDate());
//        velocityContext.put("pkColumn", genTable.getPkColumn());
//        velocityContext.put("importList", getImportList(genTable));
//        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName));
        velocityContext.put("columns", genTable.getTableColumns());
        velocityContext.put("filterColumns", filterTableColumn(genTable.getTableColumns()));
        velocityContext.put("table", genTable);
//        velocityContext.put("dicts", getDicts(genTable));
        velocityContext.put("tableName", genTable.getTableName());

        return velocityContext;
    }

    private static List<GenerateTableColumn> filterTableColumn(List<GenerateTableColumn> tableColumns) {
        return tableColumns.stream().filter(item -> {
            return !GeneratorConstants.FILTER_COLUMN.contains(item.getColumnName());
        }).collect(Collectors.toList());
    }
}
