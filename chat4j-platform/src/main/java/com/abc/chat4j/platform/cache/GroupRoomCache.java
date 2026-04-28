package com.abc.chat4j.platform.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.core.cache.AbstractRedisStringCache;
import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.service.GroupRoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GroupRoomCache extends AbstractRedisStringCache<Long, GroupRoom> {

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.GROUP_ROOM_ID, id);
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.GROUP_ROOM_ID_EXPIRE_TIME * 60;
    }

    @Override
    protected Map<Long, GroupRoom> load(List<Long> ids) {
        GroupRoomService groupRoomService = SpringUtil.getBean(GroupRoomService.class);
        return groupRoomService.selectGroupRoomListByRoomIds(ids).stream().collect(Collectors.toMap(GroupRoom::getRoomId, Function.identity()));
    }
}
