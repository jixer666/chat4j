package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.system.domain.dto.DictDataDTO;
import com.abc.chat4j.system.domain.entity.DictData;
import com.abc.chat4j.system.domain.vo.DictDataVO;

import java.util.List;

public class DictDataConvert {
    public static DictData buildDefaultDictDataByDictDataDTO(DictDataDTO dictDataDTO) {
        DictData dictData = BeanUtil.copyProperties(dictDataDTO, DictData.class);
        dictData.setDictDataId(IdUtils.getId());
        dictData.setUserId(SecurityUtils.getUserId());
        dictData.setCommonParams();

        return dictData;
    }

    public static List<DictDataVO> converDictDataVoListByDictDataList(List<DictData> dictDataList) {
        return BeanUtil.copyToList(dictDataList, DictDataVO.class);
    }

}
