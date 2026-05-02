package com.abc.chat4j.platform.controller;

import com.abc.chat4j.common.domain.vo.ApiResult;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.domain.dto.FriendApplyDTO;
import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.vo.FriendApplyVO;
import com.abc.chat4j.platform.service.FriendApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "好友申请接口")
@RestController
@RequestMapping("/im/friendApply")
public class FriendApplyController {

    @Autowired
    private FriendApplyService friendApplyService;

    @ApiOperation("发送好友申请")
    @PostMapping("/send")
    public ApiResult<FriendApplyVO> sendFriendApply(@RequestBody FriendApplyDTO friendApplyDTO) {
        friendApplyDTO.setUserId(SecurityUtils.getUserId());
        FriendApplyVO friendApplyVO = friendApplyService.sendFriendApply(friendApplyDTO);
        return ApiResult.success(friendApplyVO);
    }

    @ApiOperation("拉取离线好友申请")
    @PostMapping("/loadOfflineList")
    public ApiResult<List<FriendApplyVO>> loadOfflineList(@RequestBody FriendApplyPullDTO friendApplyPullDTO) {
        List<FriendApplyVO> friendApplyList = friendApplyService.selectOfflineFriendApplyList(friendApplyPullDTO);
        return ApiResult.success(friendApplyList);
    }

    @ApiOperation("同意/拒绝好友审核")
    @PostMapping("/operation")
    public ApiResult<FriendApplyVO> operationFriendApply(@RequestBody FriendApplyDTO friendApplyDTO) {
        FriendApplyVO friendApply = friendApplyService.operationFriendApply(friendApplyDTO);
        return ApiResult.success(friendApply);
    }

}
