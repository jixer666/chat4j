package com.abc.chat4j.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

import java.util.List;

@Data
public class DictDTO {

    private Long dictId;

    private String dictName;

    private String dictKey;

    private Integer status;

    // 用于批量删除
    private List<Long> dictIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isNotEmpty(dictId, "字典ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isNotEmpty(dictName, "字典名称不能为空");
        AssertUtils.isNotEmpty(dictKey, "字典Key不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "字典参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(dictIds), "字典ID列表不能为空");
    }
}
