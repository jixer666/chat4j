package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
public class RoomCreateContext {

    private Integer type;

    private Long uid1;

    private Long uid2;

    // 群聊成员列表
    private List<Long> userIdList;

    // 创建者（用于创建群聊）
    private Long userId;

    // 流程中设置
    private Long roomId;

}
