package com.abc.chat4j.system.util;

import cn.hutool.core.collection.CollUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.constant.GeneratorConstants;
import com.abc.chat4j.common.util.StringUtils;
import com.abc.chat4j.system.domain.entity.GenerateTable;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;
import java.util.List;

public class GenerateUtils {

    public static String getClassName(String tableName) {
        if (GeneratorConstants.autoRemovePre && tableName.startsWith(GeneratorConstants.tablePrefix)) {
            tableName = tableName.replace(GeneratorConstants.tablePrefix, "");
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return packageName.substring(lastIndex + 1);
    }

    public static String getBusinessName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public static String getFunctionName(String tableComment) {
        return RegExUtils.replaceAll(tableComment, "(?:表)", "");
    }


    public static String getColumnType(String columnType) {
        if (columnType.contains("(")) {
            return StringUtils.substringBefore(columnType, "(");
        }
        return columnType;
    }

    public static String getJavaType(String columnType) {
        if (GeneratorConstants.COLUMN_TYPE_STR.contains(columnType)) {
            return GeneratorConstants.TYPE_STRING;
        } else if (GeneratorConstants.COLUMN_TYPE_TEXT.contains(columnType)) {
            return GeneratorConstants.TYPE_STRING;
        } else if (GeneratorConstants.COLUMN_TYPE_TIME.contains(columnType)) {
            return GeneratorConstants.TYPE_DATE;
        } else if (GeneratorConstants.COLUMN_TYPE_INT.contains(columnType)) {
            return GeneratorConstants.TYPE_INTEGER;
        } else if (GeneratorConstants.COLUMN_TYPE_BIGINT.contains(columnType)) {
            return GeneratorConstants.TYPE_LONG;
        } else if (GeneratorConstants.COLUMN_TYPE_FLOAT.contains(columnType)) {
            return GeneratorConstants.TYPE_BIGDECIMAL;
        }
        return GeneratorConstants.TYPE_STRING;
    }

    public static List<String> getTemplateList() {
        return Arrays.asList(
                "vm/java/entity.java.vm",
                "vm/java/controller.java.vm",
                "vm/java/service.java.vm",
                "vm/java/serviceImpl.java.vm",
                "vm/java/mapper.java.vm",
                "vm/java/dto.java.vm",
                "vm/java/vo.java.vm",
                "vm/java/convert.java.vm",
                "vm/xml/mapper.xml.vm",
                "vm/js/api.js.vm",
                "vm/vue/index.vue.vm"
        );
    }


    public static String getGenerateFileName(String template, GenerateTable genTable) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = genTable.getPackageName();
        // 模块名
        String moduleName = genTable.getModuleName();
        // 大写类名
        String className = genTable.getClassName();
        // 业务名称
        String businessName = genTable.getBusinessName();

        String javaPath = GeneratorConstants.PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mybatisPath = GeneratorConstants.MYBATIS_PATH + "/" + moduleName;
        String vuePath = "vue";

        if (template.contains("entity.java.vm")) {
            fileName = StringUtils.format("{}/domain/entity/{}.java", javaPath, className);
        } else if (template.contains("vo.java.vm")) {
            fileName = StringUtils.format("{}/domain/vo/{}VO.java", javaPath, className);
        } else if (template.contains("dto.java.vm")) {
            fileName = StringUtils.format("{}/domain/dto/{}DTO.java", javaPath, className);
        } else if (template.contains("convert.java.vm")) {
            fileName = StringUtils.format("{}/convert/{}Convert.java", javaPath, className);
        }else if (template.contains("mapper.java.vm")) {
            fileName = StringUtils.format("{}/mapper/{}Mapper.java", javaPath, className);
        } else if (template.contains("service.java.vm")) {
            fileName = StringUtils.format("{}/service/{}Service.java", javaPath, className);
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = StringUtils.format("{}/service/impl/{}ServiceImpl.java", javaPath, className);
        } else if (template.contains("controller.java.vm")) {
            fileName = StringUtils.format("{}/controller/{}Controller.java", javaPath, className);
        } else if (template.contains("mapper.xml.vm")) {
            fileName = StringUtils.format("{}/{}Mapper.xml", mybatisPath, className);
        } else if (template.contains("api.js.vm")) {
            fileName = StringUtils.format("{}/api/{}/{}.js", vuePath, moduleName, businessName);
        } else if (template.contains("index.vue.vm")) {
            fileName = StringUtils.format("{}/views/{}/{}/index.vue", vuePath, moduleName, businessName);
        }

        return fileName;
    }

    public static String getZipFilename(List<GenerateTable> tables) {
        if (CollUtil.isEmpty(tables)) {
            return CommonConstants.SYSTEM_NAME;
        }
        return tables.get(0).getTableName() + (tables.size() == 1 ? ".zip" : "等.zip");
    }
}
