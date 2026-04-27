package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.domain.ConversationQueryContext;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.abc.chat4j.platform.mapper.ConversationMapper;
import com.abc.chat4j.platform.service.ConversationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Override
    public List<ConversationVO> selectConversationList(ConversationPullDTO conversationPullDTO) {
        checkConversationPullDTOParams(conversationPullDTO);

        ConversationQueryContext context = new ConversationQueryContext();
        context.setUserId(SecurityUtils.getUserId());
        context.setMinConversationId(context.getMinConversationId());
        List<Conversation> conversationList = selectConversationList(context);

        return conversationList.stream().map(this::buildConversationVO).collect(Collectors.toList());
    }

    private void checkConversationPullDTOParams(ConversationPullDTO conversationPullDTO) {
        AssertUtils.isNotEmpty(conversationPullDTO, "参数不能为空");
        if (Objects.isNull(conversationPullDTO.getMinConversationId())) {
            conversationPullDTO.setMinConversationId(0L);
        }
    }

    private ConversationVO buildConversationVO(Conversation conversation) {


        return null;
    }


    private List<Conversation> selectConversationList(ConversationQueryContext context) {
        return conversationMapper.selectConversationList(context);
    }
}
