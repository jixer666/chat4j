package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_room_member")
public class RoomMember extends BaseEntity {

    @TableId
    private Long roomMemberId;

    private Long userId;

    private Long groupId;

    private Long roomId;

}
