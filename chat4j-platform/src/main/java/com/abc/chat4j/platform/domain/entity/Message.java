package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_message")
public class Message extends BaseEntity {

    @TableId
    private Long msgId;

    private String tempMsgId;

    private Long userId;

    private Integer type;

    private String content;

    private Long roomId;

}
