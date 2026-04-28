package com.abc.chat4j.platform.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.core.cache.AbstractRedisStringCache;
import com.abc.chat4j.platform.domain.entity.Room;
import com.abc.chat4j.platform.service.RoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomCache extends AbstractRedisStringCache<Long, Room> {

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.ROOM_ID, id);
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.ROOM_ID_EXPIRE_TIME * 60;
    }

    @Override
    protected Map<Long, Room> load(List<Long> ids) {
        RoomService roomService = SpringUtil.getBean(RoomService.class);
        return roomService.selectRoomListByRoomIds(ids).stream().collect(Collectors.toMap(Room::getRoomId, Function.identity()));
    }
}
