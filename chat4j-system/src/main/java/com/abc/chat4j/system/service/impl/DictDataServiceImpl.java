package com.abc.chat4j.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.core.service.BaseServiceImpl;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.convert.DictDataConvert;
import com.abc.chat4j.system.domain.dto.DictDataDTO;
import com.abc.chat4j.system.domain.entity.DictData;
import com.abc.chat4j.system.domain.vo.DictDataVO;
import com.abc.chat4j.system.mapper.DictDataMapper;
import com.abc.chat4j.system.service.DictDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictDataServiceImpl extends BaseServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Autowired
    private DictDataMapper dictDataMapper;

    @Override
    public PageResult getDictDataPageWithUiParam(DictDataDTO dictDataDTO) {
        startPage();
        List<DictData> dictDatas = dictDataMapper.selectDictDataList(dictDataDTO);
        List<DictDataVO> dictDataVOList = pageList2CustomList(dictDatas, (List<DictData> list) -> {
            return BeanUtil.copyToList(list, DictDataVO.class);
        });

        return buildPageResult(dictDataVOList);
    }

    @Override
    public void updateDictData(DictDataDTO dictDataDTO) {
        dictDataDTO.checkUpdateParams();
        DictData dictData = dictDataMapper.selectById(dictDataDTO.getDictDataId());
        AssertUtils.isNotEmpty(dictData, "菜单不存在");
        BeanUtils.copyProperties(dictDataDTO, dictData);
        dictDataMapper.updateById(dictData);
    }

    @Override
    public void saveDictData(DictDataDTO dictDataDTO) {
        dictDataDTO.checkSaveParams();
        DictData dictData = DictDataConvert.buildDefaultDictDataByDictDataDTO(dictDataDTO);
        dictDataMapper.insert(dictData);
    }

    @Override
    public void deleteDictData(DictDataDTO dictDataDTO) {
        dictDataDTO.checkDeleteParams();

        dictDataMapper.deleteBatchIds(dictDataDTO.getDictDataIds());
    }

    @Override
    public List<DictDataVO> getDictDataByDictKey(String dictKey) {
        AssertUtils.isNotEmpty(dictKey, "字典Key不能为空");
        List<DictData> dictDataList = dictDataMapper.selectDictDataByDictKey(dictKey);

        return DictDataConvert.converDictDataVoListByDictDataList(dictDataList);
    }
}
