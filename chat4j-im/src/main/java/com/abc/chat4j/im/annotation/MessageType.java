package com.abc.chat4j.im.annotation;

import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageType {

    ImMessageTypeEnum type();

}
