package com.abc.chat4j.platform.domain.vo;

import com.abc.chat4j.im.domain.entity.MessageUserInfo;
import lombok.Data;

import java.util.Date;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Data
public class MessageVO {

    private Long msgId;

    private Long tempMsgId;

    private Long userId;

    private MessageUserInfo userInfo;

    private Integer type;

    private String content;

    private Long roomId;

    private Date createTime;

    private Integer status;

}
