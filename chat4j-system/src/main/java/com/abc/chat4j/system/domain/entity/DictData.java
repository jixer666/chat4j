package com.abc.chat4j.system.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("tb_dict_data")
@AllArgsConstructor
@NoArgsConstructor
public class DictData extends BaseEntity {

    @TableId
    private Long dictDataId;

    private Long dictId;

    private String dictLabel;

    private String dictValue;

    private Long userId;

}
