package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.mapper.UserFriendMapper;
import com.abc.chat4j.platform.service.UserFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend> implements UserFriendService {
}
