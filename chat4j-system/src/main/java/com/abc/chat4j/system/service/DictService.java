package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.DictDTO;
import com.abc.chat4j.system.domain.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DictService extends IService<Dict> {

    PageResult getDictPageWithUiParam(DictDTO dictDTO);

    void updateDict(DictDTO dictDTO);

    void saveDict(DictDTO dictDTO);

    void deleteDict(DictDTO dictDTO);
}
