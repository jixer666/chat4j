package com.abc.chat4j.system.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.core.cache.AbstractRedisStringCache;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserCache extends AbstractRedisStringCache<Long, User> {

    @Override
    protected String getKey(Long id) {
        return CacheConstants.getFinalKey(CacheConstants.USER_ID, id);
    }

    @Override
    protected Long getExpireSeconds() {
        return CacheConstants.USER_ID_EXPIRE_TIME * 60;
    }

    @Override
    protected Map<Long, User> load(List<Long> ids) {
        UserService userService = SpringUtil.getBean(UserService.class);
        return userService.getUserListByIds(ids).stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
    }
}
