package com.abc.chat4j.system.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.convert.FileConvert;
import com.abc.chat4j.system.domain.dto.OssFileUploadDTO;
import com.abc.chat4j.system.domain.dto.OssFileDTO;
import com.abc.chat4j.system.domain.entity.File;
import com.abc.chat4j.system.domain.vo.FileVO;
import com.abc.chat4j.system.factory.OssStrategyFactory;
import com.abc.chat4j.system.service.FileService;
import com.abc.chat4j.system.service.OssService;
import com.abc.chat4j.system.strategy.oss.OssStrategy;
import com.abc.chat4j.system.util.OssFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: LiJunXi
 * @Description:
 * @Date: 2025-08-22  21:34
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private FileService fileService;

    @Value("${oss.type}")
    private Integer ossType;

    @Override
    public FileVO uploadOss(OssFileUploadDTO req) {
        log.info("开始上传文件, MD5: {}", req.getFileMd5());
        // 保存到OSS
        File saveFile = FileConvert.buildNewFileByReq(req, ossType);
        OssFileDTO ossFile = FileConvert.buildByFile(saveFile, req.getFile());
        log.info("开始上传OSS文件，文件: {}", ossFile);
        saveToOss(ossFile);
        // 保存到数据库
        saveFile.setFilePath(ossFile.getFilePath());
        log.info("开始文件保存数据库，文件: {}", saveFile);
        fileService.save(saveFile);
        String downloadUrl = OssFileUtil.getFileDownloadUrl(saveFile.getFileId());
        log.info("文件上传成功，文件下载链接: {}", downloadUrl);
        return FileConvert.buildFileVO(saveFile, downloadUrl);
    }


    @Override
    public ResponseEntity<byte[]> downloadOss(Long fileId) {
        File fileEntity = fileService.checkAndGet(fileId);
        byte[] fileData = downloadByOss(fileEntity);
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileEntity.getFilename(), StandardCharsets.UTF_8)
                .build();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }


    private void saveToOss(OssFileDTO ossFile) {
        OssStrategy ossStrategy = getOssStrategy();
        ossStrategy.saveFile(ossFile);
    }

    private byte[] downloadByOss(File file) {
        OssStrategy ossStrategy = getOssStrategy();
        return ossStrategy.getFile(file);
    }

    private OssStrategy getOssStrategy() {
        OssStrategy ossStrategy = OssStrategyFactory.getOssStrategy(ossType);
        AssertUtils.isNotEmpty(ossStrategy, "OSS存储类型不存在");
        return ossStrategy;
    }
}
