package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.DictDataDTO;
import com.abc.chat4j.system.domain.entity.DictData;
import com.abc.chat4j.system.domain.vo.DictDataVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DictDataService extends IService<DictData> {

    PageResult getDictDataPageWithUiParam(DictDataDTO dictDataDTO);

    void updateDictData(DictDataDTO dictDataDTO);

    void saveDictData(DictDataDTO dictDataDTO);

    void deleteDictData(DictDataDTO dictDataDTO);

    List<DictDataVO> getDictDataByDictKey(String dictKey);
}
