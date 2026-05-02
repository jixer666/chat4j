package com.abc.chat4j.platform.domain.context;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
public class ConversationCreateContext {

    private Long roomId;

    private List<Long> userIdList;

}
