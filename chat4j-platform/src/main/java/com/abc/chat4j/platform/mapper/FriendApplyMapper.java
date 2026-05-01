package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.context.FriendApplyQueryContext;
import com.abc.chat4j.platform.domain.entity.FriendApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendApplyMapper extends BaseMapper<FriendApply> {
    List<FriendApply> selectFriendApply(FriendApplyQueryContext context);
}
