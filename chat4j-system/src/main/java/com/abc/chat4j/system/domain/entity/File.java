package com.abc.chat4j.system.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件实体
 *
 * @author LiJunXi
 * @date 2025-10-07
 */
@Data
@Builder
@TableName("tb_file")
@AllArgsConstructor
@NoArgsConstructor
public class File extends BaseEntity {

    @TableId
    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名称")
    private String filename;

    @ApiModelProperty("文件大小")
    private Long totalSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件MD5")
    private String fileMd5;

    @ApiModelProperty("OSS类型")
    private Integer ossType;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("用户ID")
    private Long userId;


}
