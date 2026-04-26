package com.abc.chat4j.system.domain.dto;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.util.AssertUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文件DTO对象
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Data
public class FileDTO {

    private Long fileId;

    private String filename;

    private Long totalSize;

    private String fileType;

    private String fileMd5;

    private Integer ossType;

    private String filePath;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private Integer ver;


    // 用于批量删除
    private List<Long> fileIds;

    public void checkUpdateParams() {
        AssertUtils.isNotEmpty(this, "文件参数不能为空");
        AssertUtils.isNotEmpty(fileId, "文件ID不能为空");
    }

    public void checkSaveParams() {
        AssertUtils.isNotEmpty(this, "文件参数不能为空");
    }

    public void checkDeleteParams() {
        AssertUtils.isNotEmpty(this, "文件参数不能为空");
        AssertUtils.isTrue(CollUtil.isNotEmpty(fileIds), "文件ID列表不能为空");
    }
}
