package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.vo.MessageVo;
import com.abc.chat4j.platform.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Api(tags = "消息接口")
@RestController
@RequestMapping("/im/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("拉取离线消息")
    @PostMapping("/loadOfflineMessage")
    public ApiResult<List<MessageVo>> loadOfflineMessage(@RequestBody MessagePullDTO messagePullDTO){
        List<MessageVo> messageList = messageService.loadOfflineMessage(messagePullDTO);
        return ApiResult.success(messageList);
    }

}
