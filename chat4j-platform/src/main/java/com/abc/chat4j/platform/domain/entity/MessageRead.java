package com.abc.chat4j.platform.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author LiJunXi
 * @date 2026/5/5
 */
@Data
@TableName("tb_message_read")
public class MessageRead {

    private Long readId;

    private Long userId;

    private Long msgId;

    private Integer device;

    private Date createTime;
}
