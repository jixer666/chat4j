package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.domain.vo.MessageVO;
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

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public ApiResult<MessageVO> sendMessage(@RequestBody ImSendInfo imSendInfo) {
        imSendInfo.setUserId(SecurityUtils.getUserId());
        MessageVO messageVo = messageService.sendMessage(imSendInfo);
        return ApiResult.success(messageVo);
    }

    @ApiOperation("拉取离线消息")
    @PostMapping("/loadOfflineList")
    public ApiResult<List<MessageVO>> loadOfflineList(@RequestBody MessagePullDTO messagePullDTO){
        List<MessageVO> messageList = messageService.selectOfflineMessageList(messagePullDTO);
        return ApiResult.success(messageList);
    }

    @ApiOperation("读取消息")
    @PostMapping("/read")
    public ApiResult<Void> readMessage(@RequestBody MessageReadDTO messageReadDTO) {
        messageReadDTO.setUserId(SecurityUtils.getUserId());
        messageService.readMessage(messageReadDTO);
        return ApiResult.success();
    }

}
