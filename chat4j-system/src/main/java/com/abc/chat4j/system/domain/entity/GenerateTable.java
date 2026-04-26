package com.abc.chat4j.system.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@TableName("tb_gen_table")
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTable extends BaseEntity {

    @TableId
    private Long genTableId;

    private String tableName;

    private String tableComment;

    private String className;

    private String packageName;

    private String functionAuthor;

    private String moduleName;

    private String functionName;

    private String businessName;

    private Long userId;

    @TableField(exist = false)
    private List<GenerateTableColumn> tableColumns;

}
