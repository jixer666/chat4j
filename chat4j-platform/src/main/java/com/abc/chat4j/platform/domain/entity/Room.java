package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_room")
public class Room extends BaseEntity {

    @TableId
    private Long roomId;

    private Integer type;

    private Date activeTime;

    private Long lastMsgId;

}
