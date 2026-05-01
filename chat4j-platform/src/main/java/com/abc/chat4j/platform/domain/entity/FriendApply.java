package com.abc.chat4j.platform.domain.entity;

import com.abc.chat4j.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_friend_apply")
public class FriendApply extends BaseEntity {

    @TableId
    private Long friendApplyId;

    private Long userId;

    private Long friendId;

    private String remark;

}
