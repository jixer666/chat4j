package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserFriendService extends IService<UserFriend> {
    UserFriend selectUserFriendByUidAndFriendId(Long userId, Long aLong);
}
