package com.abc.chat4j.common.core.threadlocal;

public class ThreadLocalTempVar {

    public static final ThreadLocal<Object> TEMP_USER_ID_VAR = new ThreadLocal<>();

    public static Object getTempUserId() {
        return TEMP_USER_ID_VAR.get();
    }

}
