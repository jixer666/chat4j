package com.abc.chat4j.system.mapper;

import com.abc.chat4j.system.domain.dto.FileDTO;
import com.abc.chat4j.system.domain.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件Mapper接口
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
    List<File> selectFileList(FileDTO fileDTO);
}
