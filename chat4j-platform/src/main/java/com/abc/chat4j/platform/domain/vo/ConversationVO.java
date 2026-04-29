package com.abc.chat4j.platform.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LiJunXi
 * @date 2026/4/27
 */
@Data
public class ConversationVO {

    private Long conversationId;

    private Long userId;

    private RoomInfoVO roomInfo;

    private Date activeTime;

    private Long lastMsgId;

    private Integer isPinned;

    private Integer isMuted;

    private Date createTime;

}
