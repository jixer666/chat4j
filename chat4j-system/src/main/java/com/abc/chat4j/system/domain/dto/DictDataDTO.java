package com.abc.chat4j.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

import java.util.List;

@Data
public class DictDataDTO {

    private Long dictDataId;

    private String dictLabel;

    private String dictValue;

    private Long dictId;

    private Integer status;

    // 用于批量删除
    private List<Long> dictDataIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isNotEmpty(dictDataId, "字典ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isNotEmpty(dictId, "字典ID不能为空");
        AssertUtils.isNotEmpty(dictLabel, "字典标签不能为空");
        AssertUtils.isNotEmpty(dictValue, "字典值不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(dictDataIds), "字典ID列表不能为空");
    }
}
