package com.abc.chat4j.im.factory;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.netty.process.MessageProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Component
public class MessageProcessFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<Integer, MessageProcess<?>> STRATEGY_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MessageType.class);
        beans.forEach((beanName, bean) -> {
            Class<?> clazz = bean.getClass();
            MessageType messageType = clazz.getAnnotation(MessageType.class);
            STRATEGY_MAP.put(messageType.type().getType(), (MessageProcess<?>) bean);
        });
    }

    public static MessageProcess<?> getService(Integer type) {
        AssertUtils.isTrue(STRATEGY_MAP.containsKey(type), "未知类型");
        return STRATEGY_MAP.get(type);
    }

}
