package com.abc.chat4j.platform.controller;

import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.service.UserFriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "好友接口")
@RestController
@RequestMapping("/im/userFriend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;

}
