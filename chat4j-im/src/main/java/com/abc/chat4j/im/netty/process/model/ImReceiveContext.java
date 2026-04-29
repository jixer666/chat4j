package com.abc.chat4j.im.netty.process.model;

import lombok.Data;

/**
 * @author LiJunXi
 * @date 2026/4/29
 */
@Data
public class ImReceiveContext  {

    private Object data;

    private ImSendUserInfo imReceiveUserInfo;

}
