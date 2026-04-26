package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.GenerateTableDTO;
import com.abc.chat4j.system.domain.entity.GenerateTable;
import com.abc.chat4j.system.domain.vo.GenerateTablePreviewVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

public interface GenerateTableService extends IService<GenerateTable> {

    PageResult getGenerateTablePageWithUiParam(GenerateTableDTO menuDTO);

    void updateGenerateTable(GenerateTableDTO menuDTO);

    void saveGenerateTable(GenerateTableDTO menuDTO);

    void deleteGenerateTable(GenerateTableDTO menuDTO);

    PageResult getGenerateDbPageWithUiParam(GenerateTableDTO generateTableTableDTO);

    void importTable(GenerateTableDTO generateTableTableDTO);

    GenerateTablePreviewVO previewCode(Long genTableId);

    GenerateTable getGenerateTableByGenTableId(Long genTableId);

    ResponseEntity<byte[]> downloadCode(GenerateTableDTO generateTableDTO);
}
