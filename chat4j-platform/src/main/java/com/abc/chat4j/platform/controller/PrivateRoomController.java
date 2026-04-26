package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.service.PrivateRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "单聊接口")
@RestController
@RequestMapping("/im/privateRoom")
public class PrivateRoomController {

    @Autowired
    private PrivateRoomService privateRoomService;

}
