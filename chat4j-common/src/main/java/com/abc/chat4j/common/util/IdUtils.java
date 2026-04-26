package com.abc.chat4j.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 基于雪花算法的ID生成器
 */
public class IdUtils {

    private static Snowflake snowflake = IdUtil.getSnowflake();

    public static Long getId() {
        return snowflake.nextId();
    }

    public static String getIdStr() {
        return snowflake.nextIdStr();
    }
}