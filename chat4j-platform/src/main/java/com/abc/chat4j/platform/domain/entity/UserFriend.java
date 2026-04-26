package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_user_friend")
public class UserFriend extends BaseEntity {

    @TableId
    private Long userFriendId;

    private Long userId;

    private Long friendId;

    private String remark;

}
