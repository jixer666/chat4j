package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.dto.UserFriendPullDTO;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserFriendService extends IService<UserFriend> {
    UserFriend selectUserFriendByUidAndFriendId(Long userId, Long aLong);

    List<ImUserVO> selectOfflineUserFriendList(UserFriendPullDTO userFriendPullDTO);
}
