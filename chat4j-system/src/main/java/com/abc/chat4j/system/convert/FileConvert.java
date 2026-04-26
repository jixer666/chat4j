package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.system.domain.dto.FileDTO;
import com.abc.chat4j.system.domain.dto.OssFileUploadDTO;
import com.abc.chat4j.system.domain.dto.OssFileDTO;
import com.abc.chat4j.system.domain.entity.File;
import com.abc.chat4j.system.domain.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件转换器
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Slf4j
public class FileConvert {

    public static File buildDefaultFileByFileDTO(FileDTO fileDTO) {
        File file = BeanUtil.copyProperties(fileDTO, File.class);
        file.setFileId(IdUtils.getId());
        file.setCommonParams();

        return file;
    }

    public static OssFileDTO buildByFile(File fileEntity, MultipartFile file) {
        OssFileDTO ossFileDto = BeanUtil.copyProperties(fileEntity, OssFileDTO.class);
        try {
            ossFileDto.setFileData(file.getBytes());
            ossFileDto.setMultipartFile(file);
        } catch (Exception e){
            log.error("创建OSSFileDto对象出错，原因：{}", e.getMessage());
            throw new GlobalException("创建对象出错");
        }
        return ossFileDto;
    }

    public static File buildOtherFile(File fileEntity) {
        File saveFile = BeanUtil.copyProperties(fileEntity, File.class);
        saveFile.setFileId(IdUtils.getId());
        saveFile.setUserId(SecurityUtils.getUserId());
        saveFile.setCommonParams();

        return saveFile;
    }

    public static File buildNewFileByReq(OssFileUploadDTO req, Integer ossType) {
        File fileEntity = BeanUtil.copyProperties(req, File.class);
        fileEntity.setFileId(IdUtils.getId());
        fileEntity.setOssType(ossType);
        fileEntity.setTotalSize(req.getFile().getSize());
        fileEntity.setUserId(SecurityUtils.getUserId());
        fileEntity.setFilename(req.getFile().getOriginalFilename());
        fileEntity.setCommonParams();

        return fileEntity;
    }

    public static FileVO buildFileVO(File saveFile, String downloadUrl) {
        FileVO fileVo = BeanUtil.copyProperties(saveFile, FileVO.class);
        fileVo.setDownloadUrl(downloadUrl);

        return fileVo;
    }

}
