package com.abc.chat4j.im.netty.process;

import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/5/4
 */
@Component
@MessageType(type = ImMessageTypeEnum.CONVERSATION_CREATE)
public class ConversationCreateProcess extends MessageProcess<Object> {
}
