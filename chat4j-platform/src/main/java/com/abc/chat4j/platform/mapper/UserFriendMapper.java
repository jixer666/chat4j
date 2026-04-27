package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {
    UserFriend selectByUidAndFriendId(@Param("userId") Long userId,
                                      @Param("friendId") Long friendId);
}
