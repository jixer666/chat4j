package com.abc.chat4j.im.netty.process.model;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Data
public class AuthorizationMessage {

    private String token;

    private Integer device;

}
