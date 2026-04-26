package com.abc.chat4j.system.service;

import com.abc.chat4j.common.domain.vo.PageResult;
import com.abc.chat4j.system.domain.dto.FileDTO;
import com.abc.chat4j.system.domain.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文件接口
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
public interface FileService extends IService<File> {

    PageResult getFilePageWithUiParam(FileDTO fileDTO);

    void updateFile(FileDTO fileDTO);

    void saveFile(FileDTO fileDTO);

    void deleteFile(FileDTO fileDTO);

    File checkAndGet(Long fileId);

}
