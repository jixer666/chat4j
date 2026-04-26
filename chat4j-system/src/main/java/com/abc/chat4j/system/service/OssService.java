package com.abc.chat4j.system.service;

import com.abc.chat4j.system.domain.vo.FileVO;
import com.abc.chat4j.system.domain.dto.OssFileUploadDTO;
import org.springframework.http.ResponseEntity;

public interface OssService {

    FileVO uploadOss(OssFileUploadDTO req);

    ResponseEntity<byte[]> downloadOss(Long fileId);

}
