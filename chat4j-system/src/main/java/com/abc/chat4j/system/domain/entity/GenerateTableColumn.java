package com.abc.chat4j.system.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tb_gen_table_column")
@AllArgsConstructor
@NoArgsConstructor
public class GenerateTableColumn extends BaseEntity {

    @TableId
    private Long genTableColumnId;

    private Long genTableId;

    private String columnName;

    private String columnComment;

    private String columnType;

    private String javaType;

    private String javaField;

    private Integer isPk;

    private Integer isRequire;

    private Integer sort;

    private Integer isIncrement;


}
