package com.abc.chat4j.common.constant;

public class CacheConstants {

    public static final String SYSTEM_NAME = "chat4j:";

    public static final String LOGIN_TOKEN_KEY = "loginToken:%s";

    public static final String CAPTCHA_UUID = "captchaUuid:%s";
    public static final Long CAPTCHA_UUID_EXPIRE_TIME = 5L;

    public static final String EMAIL_UUID = "emailUuid:%s";
    public static final Long EMAIL_UUID_EXPIRE_TIME = 5L;

    public static final String USER_ID = "userId:%d";
    public static final Long USER_ID_EXPIRE_TIME = 8L;

    public static final String ROOM_ID = "roomId:%d";
    public static final Long ROOM_ID_EXPIRE_TIME = 8L;

    public static final String PRIVATE_ROOM_ID = "privateRoomId:%d";
    public static final Long PRIVATE_ROOM_ID_EXPIRE_TIME = 8L;

    public static final String GROUP_ROOM_ID = "groupRoomId:%d";
    public static final Long GROUP_ROOM_ID_EXPIRE_TIME = 8L;

    public static String getFinalKey(String key, Object ...values) {
        return String.format(SYSTEM_NAME + key, values);
    }





}
