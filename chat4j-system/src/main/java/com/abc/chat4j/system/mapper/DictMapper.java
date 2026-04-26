package com.abc.chat4j.system.mapper;

import com.abc.chat4j.system.domain.dto.DictDTO;
import com.abc.chat4j.system.domain.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> selectDictList(DictDTO dictDTO);
}
