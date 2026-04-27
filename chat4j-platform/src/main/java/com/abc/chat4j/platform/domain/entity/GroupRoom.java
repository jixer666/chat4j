package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_group_room")
public class GroupRoom extends BaseEntity {

    @TableId
    private Long groupRoomId;

    private Long roomId;

    private Long groupId;

    private Long userId;

    private String name;

    private String avatar;

}
