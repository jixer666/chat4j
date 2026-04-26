package com.abc.chat4j.system.factory;

import com.abc.chat4j.system.domain.enums.OssTypeEnum;
import com.abc.chat4j.system.strategy.oss.OssStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OssStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    private static Map<Integer, OssStrategy> OSS_STRATEGY_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(OssStrategy.class).forEach((k,v)->{
            OSS_STRATEGY_MAP.put(OssTypeEnum.getTypeByOssKey(k), v);
        });
    }

    public static OssStrategy getOssStrategy(Integer ossType) {
        return OSS_STRATEGY_MAP.get(ossType);
    }

}
