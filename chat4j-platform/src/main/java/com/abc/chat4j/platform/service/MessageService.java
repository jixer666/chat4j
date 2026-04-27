package com.abc.chat4j.platform.service;

import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.platform.domain.vo.MessageVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
public interface MessageService extends IService<Message> {
    List<MessageVO> selectOfflineMessageList(MessagePullDTO messagePullDTO);

    MessageVO sendMessage(ImSendInfo imSendInfo);
}
