package com.abc.chat4j.im.netty.process.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyMessage {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long friendApplyId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long friendId;

    private Integer status;

    // 接收方需要的用户信息
    private String nickname;

    private String avatar;

}
