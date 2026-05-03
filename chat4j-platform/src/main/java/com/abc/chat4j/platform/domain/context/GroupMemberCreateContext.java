package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/3
 */
@Data
public class GroupMemberCreateContext {

    private Long roomId;

    private Long groupRoomId;

    private List<Long> userIdList;
}
