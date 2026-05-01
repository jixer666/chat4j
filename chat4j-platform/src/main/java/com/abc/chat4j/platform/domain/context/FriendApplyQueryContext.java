package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.Date;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
public class FriendApplyQueryContext {

    private Long friendApplyId;

    private Long userId;

    private Long friendId;

    private Date minUpdateTime;

}
