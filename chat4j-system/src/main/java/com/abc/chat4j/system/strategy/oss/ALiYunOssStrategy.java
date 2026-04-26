package com.abc.chat4j.system.strategy.oss;

import com.abc.chat4j.system.domain.dto.OssFileDTO;
import com.abc.chat4j.system.domain.entity.File;
import org.springframework.stereotype.Service;

/**
 * @Author: LiJunXi
 * @Description:
 * @Date: 2025-08-24  14:37
 */
@Service("aLiYunOssStrategy")
public class ALiYunOssStrategy implements OssStrategy {

    @Override
    public void saveFile(OssFileDTO file) {

    }

    @Override
    public byte[] getFile(File fileEntity) {
        return new byte[0];
    }

    @Override
    public void deleteFile(File fileEntity) {

    }
}

