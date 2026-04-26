package com.abc.chat4j.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.domain.entity.GenerateTableColumn;
import lombok.Data;

import java.util.List;

@Data
public class GenerateTableDTO {

    private Long genTableId;

    private String tableName;

    private String tableComment;

    private String className;

    private String packageName;

    private String functionAuthor;

    private String moduleName;

    private String functionName;

    private String businessName;

    private List<GenerateTableColumn> tableColumns;

    // 用于批量删除
    private List<Long> genTableIds;

    // 用于导入表结构
    private List<String> tableNames;

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "代码导入参数不能为空");
        AssertUtils.isNotEmpty(tableName, "表名称不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "代码导入参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(genTableIds), "表ID列表不能为空");
    }

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "代码导入参数不能为空");
        AssertUtils.isNotEmpty(genTableId, "表ID不能为空");
    }

    public void checkImportParams() {
        AssertUtils.isNotEmpty(this, "代码导入参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(tableNames), "表结构名称列表不能为空");
    }
}
