package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.dto.UserFriendPullDTO;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.UserFriendVO;
import com.abc.chat4j.platform.service.UserFriendService;
import com.abc.chat4j.system.domain.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "好友接口")
@RestController
@RequestMapping("/im/userFriend")
public class UserFriendController {

    @Autowired
    private UserFriendService userFriendService;

    @ApiOperation("搜索用户")
    @PostMapping("/search")
    public ApiResult<UserFriendVO> searchUser(@RequestBody UserDTO userDTO) {
        UserFriendVO userVO = userFriendService.searchUserFriend(userDTO);
        return ApiResult.success(userVO);
    }

    @ApiOperation("拉取离线好友列表")
    @PostMapping("/loadOfflineList")
    public ApiResult<List<ImUserVO>> loadOfflineList(@RequestBody FriendApplyPullDTO friendApplyPullDTO) {
        List<ImUserVO> userList = userFriendService.selectOfflineUserFriendList(friendApplyPullDTO);
        return ApiResult.success(userList);
    }

}
