package com.abc.chat4j.system.strategy.oss;

import com.abc.chat4j.system.domain.dto.OssFileDTO;
import com.abc.chat4j.system.domain.entity.File;

public interface OssStrategy {

    void saveFile(OssFileDTO file);

    byte[] getFile(File fileEntity);

    void deleteFile(File fileEntity);


}
