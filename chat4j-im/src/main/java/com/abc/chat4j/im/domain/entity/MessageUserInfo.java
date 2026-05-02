package com.abc.chat4j.im.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserInfo {

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

}
