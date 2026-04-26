package com.abc.chat4j.common.constant;

import java.util.Arrays;
import java.util.List;

public class GeneratorConstants {

    public static final String AUTHOR = "LiJunXi";

    public static final String PACKAGE_NAME = "com.abc.system";

    public static final String tablePrefix = "tb_";

    public static final Boolean autoRemovePre = true;

    /** 数据库字符串类型 */
    public static final List<String> COLUMN_TYPE_STR = Arrays.asList("char", "varchar", "nvarchar", "varchar2");

    /** 数据库文本类型 */
    public static final List<String> COLUMN_TYPE_TEXT = Arrays.asList("tinytext", "text", "mediumtext", "longtext");

    /** 数据库时间类型 */
    public static final List<String> COLUMN_TYPE_TIME = Arrays.asList("datetime", "time", "date", "timestamp");

    /** 数据库数字类型 */
    public static final List<String> COLUMN_TYPE_INT = Arrays.asList("tinyint", "smallint", "mediumint", "int",
            "number", "integer", "bit");

    public static final List<String> COLUMN_TYPE_BIGINT = Arrays.asList("bigint");

    /** 数据库数字符类型 */
    public static final List<String> COLUMN_TYPE_FLOAT = Arrays.asList("float", "double", "decimal");

    /** 字符串类型 */
    public static final String TYPE_STRING = "String";

    /** 整型 */
    public static final String TYPE_INTEGER = "Integer";

    /** 长整型 */
    public static final String TYPE_LONG = "Long";

    /** 浮点型 */
    public static final String TYPE_DOUBLE = "Double";

    /** 高精度计算类型 */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** 时间类型 */
    public static final String TYPE_DATE = "Date";

    public static final List<String> FILTER_COLUMN = Arrays.asList("create_time", "update_time", "status", "ver");

    /** 项目空间路径 */
    public static final String PROJECT_PATH = "main/java";

    /** mybatis空间路径 */
    public static final String MYBATIS_PATH = "main/resources/mapper";


}
