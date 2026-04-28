package com.abc.chat4j.platform.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserInfo {

    private String nickname;

    private String avatar;

}
