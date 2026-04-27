package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.abc.chat4j.platform.service.ConversationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "会话接口")
@RestController
@RequestMapping("/im/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @ApiOperation("拉取离线会话列表")
    @PostMapping("/loadOfflineList")
    public ApiResult<List<ConversationVO>> loadOfflineList(@RequestBody ConversationPullDTO conversationPullDTO) {
        List<ConversationVO> conversationList = conversationService.selectConversationList(conversationPullDTO);
        return ApiResult.success(conversationList);
    }
}
