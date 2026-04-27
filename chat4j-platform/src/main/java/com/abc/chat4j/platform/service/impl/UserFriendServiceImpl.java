package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.mapper.UserFriendMapper;
import com.abc.chat4j.platform.service.UserFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend> implements UserFriendService {

    @Autowired
    private UserFriendMapper userFriendMapper;

    @Override
    public UserFriend selectUserFriendByUidAndFriendId(Long userId, Long friendId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(friendId, "好友ID不能为空");
        return userFriendMapper.selectByUidAndFriendId(userId, friendId);
    }
}
