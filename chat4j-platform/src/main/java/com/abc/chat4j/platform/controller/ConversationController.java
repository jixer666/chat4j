package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.service.ConversationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "会话接口")
@RestController
@RequestMapping("/im/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

}
