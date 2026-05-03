package com.abc.chat4j.im.netty.process.model;

import lombok.Data;

import java.util.Date;

/** 消息回调上下文
 * @author LiJunXi
 * @date 2026/5/3
 */
@Data
public class ImCallbackContext {

    private Long msgId;

    private Long roomId;

    private Date activeTime;

}
