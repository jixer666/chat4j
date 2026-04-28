package com.abc.chat4j.im.netty.process.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiJunXi
 * @date 2026/4/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImSendUserInfo {

    private Long userId;

    private Integer device;
}
