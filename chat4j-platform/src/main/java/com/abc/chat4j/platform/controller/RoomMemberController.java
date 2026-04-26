package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.RoomMember;
import com.abc.chat4j.platform.service.RoomMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "群成员接口")
@RestController
@RequestMapping("/im/roomMember")
public class RoomMemberController {

    @Autowired
    private RoomMemberService roomMemberService;

}
