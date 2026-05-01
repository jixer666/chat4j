package com.abc.chat4j.platform.domain.vo;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
public class UserFriendVO {

    private Long userFriendId;

    private Long userId;

    private Long friendId;


    // 对方具体的信息
    private String nickname;

    private String avatar;

    // 是否是好友
    private Boolean isFriend;
}
