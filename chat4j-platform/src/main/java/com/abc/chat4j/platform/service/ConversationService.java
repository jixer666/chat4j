package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.context.ConversationCreateContext;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface ConversationService extends IService<Conversation> {
    List<ConversationVO> selectConversationList(ConversationPullDTO conversationPullDTO);

    void updateActiveTimeByConversationId(Long conversationId, Long userId, Date date);

    List<Conversation> selectConversationBydUidListAndRoomId(List<Long> userIdList, Long roomId);

    Conversation selectConversationBydUidAndRoomId(Long userId, Long roomId);

    List<Conversation> createConversation(ConversationCreateContext conversationContext);
}
