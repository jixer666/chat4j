package com.abc.chat4j.system.factory;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.domain.enums.LoginTypeEnum;
import com.abc.chat4j.system.strategy.login.AuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, AuthStrategy> LOGIN_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(AuthStrategy.class).forEach((k, v)->{
            LOGIN_MAP.put(LoginTypeEnum.getTypeByClass(k), v);
        });
    }

    public static AuthStrategy getAuthStrategy(Integer loginType){
        Integer type = LoginTypeEnum.getClassByType(loginType);
        AssertUtils.isNotEmpty(type, "认证方式不存在");
        return LOGIN_MAP.get(type);
    }



}
