package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.service.GroupRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "群聊接口")
@RestController
@RequestMapping("/im/groupRoom")
public class GroupRoomController {

    @Autowired
    private GroupRoomService groupRoomService;

}
