package com.abc.chat4j.im.netty.process;

import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.process.model.FriendApplyMessage;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Component
@MessageType(type = ImMessageTypeEnum.FRIEND_APPLY)
public class FriendApplyOperationProcess extends MessageProcess<FriendApplyMessage> {
}
