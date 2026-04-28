package com.abc.chat4j.im.netty.process;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.process.model.TextMessage;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Component
@MessageType(type = ImMessageTypeEnum.TEXT)
public class TextMessageProcess extends MessageProcess<TextMessage> {

    @Override
    public void checkMessageContentDetails(TextMessage data) {
        AssertUtils.isNotEmpty(data.getText(), "消息内容不能为空");
    }


}
