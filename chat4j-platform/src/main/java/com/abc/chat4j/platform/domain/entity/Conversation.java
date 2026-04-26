package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_conversation")
public class Conversation extends BaseEntity {

    @TableId
    private Long conversationId;

    private Long userId;

    private Long roomId;

    private Date activeTime;

    private Long lastMsgId;

}
