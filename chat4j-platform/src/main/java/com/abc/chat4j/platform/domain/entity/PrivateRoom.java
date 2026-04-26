package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
@TableName("tb_private_room")
public class PrivateRoom extends BaseEntity {

    @TableId
    private Long privateRoomId;

    private Long roomId;

    private Long userId;

    private Long friendId;

    @TableField("room_key")
    private String roomKey;

}
