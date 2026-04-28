package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.context.ConversationQueryContext;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
    List<Conversation> selectConversationList(ConversationQueryContext context);
}
