package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.UserFriendVO;
import com.abc.chat4j.system.domain.dto.UserDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserFriendService extends IService<UserFriend> {
    UserFriend selectUserFriendByUidAndFriendId(Long userId, Long aLong);

    List<ImUserVO> selectOfflineUserFriendList(FriendApplyPullDTO friendApplyPullDTO);

    UserFriendVO searchUserFriend(UserDTO userDTO);

    void saveUserFriendList(List<UserFriend> list);
}
