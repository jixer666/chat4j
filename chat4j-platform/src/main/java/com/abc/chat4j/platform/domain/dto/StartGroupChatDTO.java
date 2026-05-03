package com.abc.chat4j.platform.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/3
 */
@Data
public class StartGroupChatDTO {

    private List<Long> userIdList;

    // 后端设置
    private Long userId;

}
