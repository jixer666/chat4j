package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.service.GroupMemberService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "群成员接口")
@RestController
@RequestMapping("/im/roomMember")
public class RoomMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

}
