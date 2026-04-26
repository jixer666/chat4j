package com.abc.chat4j.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils extends FileUtil {

    /**
     * 根据日期获取文件名
     * xxx_20251022_xxxxx.xxx
     * @param prefix
     * @param extension
     * @return
     */
    public static String getFilenameByDate(String prefix, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String absolutePath = sdf.format(new Date()).replace("-", "");
        return prefix + "_" + absolutePath + "_" + RandomUtil.randomString(5) + extension;
    }

}
