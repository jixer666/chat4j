package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.Room;
import com.abc.chat4j.platform.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "房间接口")
@RestController
@RequestMapping("/im/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

}
