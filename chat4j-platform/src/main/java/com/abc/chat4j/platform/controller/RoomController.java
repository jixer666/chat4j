package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.domain.dto.RoomDTO;
import com.abc.chat4j.platform.domain.dto.StartGroupChatDTO;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.abc.chat4j.platform.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "房间接口")
@RestController
@RequestMapping("/im/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation("发起群聊")
    @PostMapping("/startGroupChat")
    public ApiResult<RoomInfoVO> startGroupChat(@RequestBody StartGroupChatDTO startGroupChatDTO) {
        startGroupChatDTO.setUserId(SecurityUtils.getUserId());
        RoomInfoVO roomInfoVO = roomService.startGroupChat(startGroupChatDTO);
        return ApiResult.success(roomInfoVO);
    }

    @ApiOperation("群成员列表")
    @PostMapping("/memberList")
    public ApiResult<List<ImUserVO>> memberList(@RequestBody RoomDTO roomDTO) {
        List<ImUserVO> userList = roomService.selectRoomMemberListByRoomId(roomDTO);
        return ApiResult.success(userList);
    }

}
