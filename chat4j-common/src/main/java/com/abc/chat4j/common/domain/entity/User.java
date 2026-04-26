package com.abc.chat4j.common.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@TableName("tb_user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @TableId
    private Long userId;

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private String email;

}
