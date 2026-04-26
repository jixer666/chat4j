package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.system.domain.dto.DictDTO;
import com.abc.chat4j.system.domain.entity.Dict;

public class DictConvert {
    public static Dict buildDefaultDictByDictDTO(DictDTO dictDTO) {
        Dict dict = BeanUtil.copyProperties(dictDTO, Dict.class);
        dict.setDictId(IdUtils.getId());
        dict.setUserId(SecurityUtils.getUserId());
        dict.setCommonParams();

        return dict;
    }
}
