package com.abc.chat4j.system.domain.dto;

import com.abc.chat4j.system.domain.entity.File;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: LiJunXi
 * @Description:
 * @Date: 2025-08-04  20:27
 */
@Slf4j
@Data
public class OssFileDTO extends File {

    private byte[] fileData;

    private MultipartFile multipartFile;

}
