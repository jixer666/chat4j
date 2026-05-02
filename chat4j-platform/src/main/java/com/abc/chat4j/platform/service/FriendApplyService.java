package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.dto.FriendApplyDTO;
import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.entity.FriendApply;
import com.abc.chat4j.platform.domain.vo.FriendApplyVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FriendApplyService extends IService<FriendApply> {

    FriendApplyVO sendFriendApply(FriendApplyDTO friendApplyDTO);

    FriendApply selectFriendApplyByUserIdAndFriendId(Long userId, Long friendId);

    List<FriendApplyVO> selectOfflineFriendApplyList(FriendApplyPullDTO friendApplyPullDTO);

    FriendApplyVO operationFriendApply(FriendApplyDTO friendApplyDTO);

}
