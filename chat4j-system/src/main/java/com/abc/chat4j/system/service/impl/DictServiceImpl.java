package com.abc.chat4j.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.core.service.BaseServiceImpl;
import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.convert.DictConvert;
import com.abc.chat4j.system.domain.dto.DictDTO;
import com.abc.chat4j.system.domain.entity.Dict;
import com.abc.chat4j.system.domain.vo.DictVO;
import com.abc.chat4j.system.mapper.DictMapper;
import com.abc.chat4j.system.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public PageResult getDictPageWithUiParam(DictDTO dictDTO) {
        startPage();
        List<Dict> dicts = dictMapper.selectDictList(dictDTO);
        List<DictVO> dictVOList = pageList2CustomList(dicts, (List<Dict> list) -> {
            return BeanUtil.copyToList(list, DictVO.class);
        });

        return buildPageResult(dictVOList);
    }

    @Override
    public void updateDict(DictDTO dictDTO) {
        dictDTO.checkUpdateParams();
        Dict dict = dictMapper.selectById(dictDTO.getDictId());
        AssertUtils.isNotEmpty(dict, "字典不存在");
        BeanUtils.copyProperties(dictDTO, dict);
        dictMapper.updateById(dict);
    }

    @Override
    public void saveDict(DictDTO dictDTO) {
        dictDTO.checkSaveParams();
        Dict dict = DictConvert.buildDefaultDictByDictDTO(dictDTO);
        dictMapper.insert(dict);
    }

    @Override
    public void deleteDict(DictDTO dictDTO) {
        dictDTO.checkDeleteParams();

        dictMapper.deleteBatchIds(dictDTO.getDictIds());
    }



}
