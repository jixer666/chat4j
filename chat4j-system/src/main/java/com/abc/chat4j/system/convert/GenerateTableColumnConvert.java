package com.abc.chat4j.system.convert;

import com.abc.chat4j.system.util.GenerateUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.system.domain.dto.GenerateTableColumnDTO;
import com.abc.chat4j.system.domain.entity.GenerateTableColumn;

public class GenerateTableColumnConvert {

    public static GenerateTableColumn buildDefaultGenerateTableColumnByGenerateTableColumnDTO(GenerateTableColumnDTO generateTableColumnDTO) {
        return null;
    }

    public static void initDefaultGenerateTableColumn(GenerateTableColumn generateTableColumn) {
        generateTableColumn.setGenTableColumnId(IdUtils.getId());
        generateTableColumn.setColumnType(GenerateUtils.getColumnType(generateTableColumn.getColumnType()));
        generateTableColumn.setJavaField(StringUtils.toCamelCase(generateTableColumn.getColumnName()));
        generateTableColumn.setJavaType(GenerateUtils.getJavaType(generateTableColumn.getColumnType()));
        generateTableColumn.setCommonParams();
    }
}
