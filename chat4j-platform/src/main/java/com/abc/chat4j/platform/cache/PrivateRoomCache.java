package com.abc.chat4j.platform.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.core.cache.AbstractRedisStringCache;
import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.service.PrivateRoomService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PrivateRoomCache extends AbstractRedisStringCache<Long, PrivateRoom> {

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.PRIVATE_ROOM_ID, id);
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.PRIVATE_ROOM_ID_EXPIRE_TIME * 60;
    }

    @Override
    protected Map<Long, PrivateRoom> load(List<Long> ids) {
        PrivateRoomService privateRoomService = SpringUtil.getBean(PrivateRoomService.class);
        return privateRoomService.selectPrivateRoomListByRoomIds(ids).stream().collect(Collectors.toMap(PrivateRoom::getRoomId, Function.identity()));
    }
}
