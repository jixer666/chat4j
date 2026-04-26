package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.mapper.ConversationMapper;
import com.abc.chat4j.platform.service.ConversationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {
}
