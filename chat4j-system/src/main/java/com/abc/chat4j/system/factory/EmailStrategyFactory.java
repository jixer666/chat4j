package com.abc.chat4j.system.factory;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.system.domain.enums.EmailTypeEnum;
import com.abc.chat4j.system.strategy.email.EmailStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmailStrategyFactory {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<Integer, EmailStrategy> EMAIL_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        applicationContext.getBeansOfType(EmailStrategy.class).forEach((k, v)->{
            EMAIL_MAP.put(EmailTypeEnum.getTypeByClass(k), v);
        });
    }

    public static EmailStrategy getEmailStrategy(Integer emailType){
        Integer type = EmailTypeEnum.getClassByType(emailType);
        AssertUtils.isNotEmpty(type, "邮箱类型不存在");
        return EMAIL_MAP.get(type);
    }



}
