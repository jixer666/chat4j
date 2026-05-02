package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.context.UserFriendQueryContext;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFriendMapper extends BaseMapper<UserFriend> {
    UserFriend selectByUidAndFriendId(@Param("userId") Long userId,
                                      @Param("friendId") Long friendId);

    List<UserFriend> selectBatchByUidAndFriendId(@Param("userFriendList") List<UserFriend> userFriendList);

    List<UserFriend> selectUserFriendList(UserFriendQueryContext context);

    void insertBatch(@Param("userFriendList") List<UserFriend> userFriendList);
}
