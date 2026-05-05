package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.service.MessageReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "消息已读接口")
@RestController
@RequestMapping("/im/message/read")
public class MessageReadController {

    @Autowired
    private MessageReadService messageReadService;


}
