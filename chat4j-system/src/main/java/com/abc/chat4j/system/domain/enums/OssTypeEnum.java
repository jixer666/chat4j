package com.abc.chat4j.system.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OssTypeEnum {

    MINIO(1, "minioOssStrategy"),
    ALIYUN(2, "aLiYunOssStrategy"),
    LOCAL(3, "localOssStrategy")
    ;

    private final Integer type;
    private final String ossKey;

    public static Integer getTypeByOssKey(String ossKey) {
        return Arrays.stream(OssTypeEnum.values()).filter(item -> item.getOssKey().equals(ossKey)).findFirst().get().getType();
    }

}
