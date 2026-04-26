/*
 Navicat Premium Dump SQL

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : chat4j

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 13/12/2025 18:52:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dict
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict`;
CREATE TABLE `tb_dict`  (
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `dict_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典Key',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_dictKey_uk`(`dict_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_dict
-- ----------------------------
INSERT INTO `tb_dict` VALUES (1974800914256699392, '通用状态', 'common_status', 1974016655841509377, '2025-10-05 19:36:43', '2025-10-05 19:36:43', 1, 1);
INSERT INTO `tb_dict` VALUES (1974826537440174080, '菜单显示状态', 'system_menu_hidden', 1974016655841509377, '2025-10-05 21:18:32', '2025-10-05 21:18:32', 1, 1);
INSERT INTO `tb_dict` VALUES (1975475788591636480, 'Java类型', 'system_java_type', 1974016655841509377, '2025-10-07 16:18:25', '2025-10-07 16:18:25', 1, 1);
INSERT INTO `tb_dict` VALUES (1975577104449929216, 'OSS类型', 'system_oss_type', 1974016655841509377, '2025-10-07 23:01:01', '2025-10-07 23:01:01', 1, 1);

-- ----------------------------
-- Table structure for tb_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict_data`;
CREATE TABLE `tb_dict_data`  (
  `dict_data_id` bigint NOT NULL COMMENT '字典数据ID',
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `dict_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`dict_data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_dict_data
-- ----------------------------
INSERT INTO `tb_dict_data` VALUES (1974809021305208832, 1974800914256699392, '正常', '1', 1974016655841509377, '2025-10-05 20:08:56', '2025-10-05 20:08:56', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1974812232862842880, 1974800914256699392, '禁用', '2', 1974016655841509377, '2025-10-05 20:21:41', '2025-10-05 20:21:41', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1974827254838091776, 1974826537440174080, '显示', '1', 1974016655841509377, '2025-10-05 21:21:23', '2025-10-05 21:21:23', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1974827293534740480, 1974826537440174080, '隐藏', '2', 1974016655841509377, '2025-10-05 21:21:32', '2025-10-05 21:21:32', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475832493416448, 1975475788591636480, 'Long', 'Long', 1974016655841509377, '2025-10-07 16:18:36', '2025-10-07 16:18:36', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475850780581888, 1975475788591636480, 'String', 'String', 1974016655841509377, '2025-10-07 16:18:40', '2025-10-07 16:18:40', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475872964255744, 1975475788591636480, 'Integer', 'Integer', 1974016655841509377, '2025-10-07 16:18:45', '2025-10-07 16:18:45', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475888554483712, 1975475788591636480, 'Double', 'Double', 1974016655841509377, '2025-10-07 16:18:49', '2025-10-07 16:18:49', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475907152027648, 1975475788591636480, 'BigDecimal', 'BigDecimal', 1974016655841509377, '2025-10-07 16:18:53', '2025-10-07 16:18:53', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475923316875264, 1975475788591636480, 'Date', 'Date', 1974016655841509377, '2025-10-07 16:18:57', '2025-10-07 16:18:57', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975475937950801920, 1975475788591636480, 'Boolean', 'Boolean', 1974016655841509377, '2025-10-07 16:19:01', '2025-10-07 16:19:01', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975804227416678400, 1975577104449929216, 'Minio', '1', 1974016655841509377, '2025-10-08 14:03:31', '2025-10-08 14:03:31', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975804437475811328, 1975577104449929216, '阿里云', '2', 1974016655841509377, '2025-10-08 14:04:21', '2025-10-08 14:04:21', 1, 1);
INSERT INTO `tb_dict_data` VALUES (1975804458749321216, 1975577104449929216, '本地', '3', 1974016655841509377, '2025-10-08 14:04:26', '2025-10-08 14:04:26', 1, 1);

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file`  (
  `file_id` bigint NOT NULL COMMENT '文件ID',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `total_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件MD5',
  `oss_type` tinyint NULL DEFAULT NULL COMMENT 'OSS类型',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_file
-- ----------------------------
INSERT INTO `tb_file` VALUES (1975796089552531456, '8c0e2877eb06f6bd84bfba168cef11df997787e7.jpg@1192w_1192h.png', 16204, 'image/png', '2e1a2494808f367b9106d513eb1c134d', 3, 'D:/project/oss/chat4j/2025/10/08/2e1a2494808f367b9106d513eb1c134d.png', 1974016655841509377, '2025-10-08 13:31:11', '2025-10-08 13:31:11', 1, 1);
INSERT INTO `tb_file` VALUES (1975798365709053952, '8c0e2877eb06f6bd84bfba168cef11df997787e7.jpg@1192w_1192h.png', 16204, 'image/png', '2e1a2494808f367b9106d513eb1c134d', 3, 'D:/project/oss/chat4j/2025/10/08/2e1a2494808f367b9106d513eb1c134d.png', 1974016655841509377, '2025-10-08 13:40:14', '2025-10-08 13:40:14', 1, 1);
INSERT INTO `tb_file` VALUES (1975798474568019968, '8c0e2877eb06f6bd84bfba168cef11df997787e7.jpg@1192w_1192h.png', 16204, 'image/png', '2e1a2494808f367b9106d513eb1c134d', 3, 'D:/project/oss/chat4j/2025/10/08/2e1a2494808f367b9106d513eb1c134d.png', 1974016655841509377, '2025-10-08 13:40:40', '2025-10-08 13:40:40', 1, 1);
INSERT INTO `tb_file` VALUES (1975799014760820736, '8c0e2877eb06f6bd84bfba168cef11df997787e7.jpg@1192w_1192h.png', 16204, 'image/png', '2e1a2494808f367b9106d513eb1c134d', 3, 'D:/project/oss/chat4j/2025/10/08/2e1a2494808f367b9106d513eb1c134d.png', 1974016655841509377, '2025-10-08 13:42:48', '2025-10-08 13:42:48', 1, 1);

-- ----------------------------
-- Table structure for tb_gen_table
-- ----------------------------
DROP TABLE IF EXISTS `tb_gen_table`;
CREATE TABLE `tb_gen_table`  (
  `gen_table_id` bigint NOT NULL COMMENT '表ID',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名称',
  `table_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表注释',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '包名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `module_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能名',
  `business_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务名',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`gen_table_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_gen_table
-- ----------------------------
INSERT INTO `tb_gen_table` VALUES (1975535443023929346, 'tb_file', '文件表', 'File', 'com.abc.system', 'LiJunXi', 'system', '文件', 'file', 1975535442990374912, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);

-- ----------------------------
-- Table structure for tb_gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `tb_gen_table_column`;
CREATE TABLE `tb_gen_table_column`  (
  `gen_table_column_id` bigint NOT NULL COMMENT '表字段ID',
  `gen_table_id` bigint NOT NULL COMMENT '表ID',
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `column_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段注释',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Java类型',
  `java_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Java字段名',
  `is_pk` tinyint NULL DEFAULT NULL COMMENT '是否主键',
  `is_require` tinyint NULL DEFAULT NULL COMMENT '是否必须',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `is_increment` tinyint NULL DEFAULT NULL COMMENT '是否自增',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`gen_table_column_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_gen_table_column
-- ----------------------------
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038208, 1975535443023929346, 'file_id', '文件ID', 'bigint', 'Long', 'fileId', 1, NULL, 1, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038209, 1975535443023929346, 'filename', '文件名称', 'varchar', 'String', 'filename', 0, NULL, 2, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038210, 1975535443023929346, 'total_size', '文件大小', 'bigint', 'Long', 'totalSize', 0, NULL, 3, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038211, 1975535443023929346, 'file_type', '文件类型', 'varchar', 'String', 'fileType', 0, NULL, 4, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038212, 1975535443023929346, 'file_md5', '文件MD5', 'varchar', 'String', 'fileMd5', 0, NULL, 5, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038213, 1975535443023929346, 'oss_type', 'OSS类型', 'tinyint', 'Integer', 'ossType', 0, NULL, 6, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038214, 1975535443023929346, 'file_path', '文件路径', 'varchar', 'String', 'filePath', 0, NULL, 7, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038215, 1975535443023929346, 'user_id', '用户ID', 'bigint', 'Long', 'userId', 0, NULL, 8, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038216, 1975535443023929346, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', 0, NULL, 9, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038217, 1975535443023929346, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', 0, NULL, 10, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038218, 1975535443023929346, 'status', '状态', 'tinyint', 'Integer', 'status', 0, NULL, 11, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);
INSERT INTO `tb_gen_table_column` VALUES (1975535443091038219, 1975535443023929346, 'ver', '版本号', 'int', 'Integer', 'ver', 0, NULL, 12, 0, '2025-10-07 20:15:28', '2025-10-07 20:15:28', 1, 1);

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单ID',
  `menu_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `menu_type` tinyint NULL DEFAULT NULL COMMENT '菜单类型',
  `order_num` int NULL DEFAULT NULL COMMENT '顺序',
  `perms` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `hidden` tinyint NULL DEFAULT NULL COMMENT '是否隐藏',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1970482232512675841, 0, '系统管理', 'admin/system', NULL, 1, 1, NULL, 'system', 1, 1974016655841509377, '2025-10-04 13:04:29', '2025-10-04 13:04:32', 1, 1);
INSERT INTO `tb_menu` VALUES (1970482232512695842, 1970482232512675841, '用户管理', 'user', 'system/user/index', 2, 1, 'system:user:list', 'user', 1, 1974016655841509377, '2025-10-04 13:04:29', '2025-10-04 13:04:32', 1, 1);
INSERT INTO `tb_menu` VALUES (1970482232512695843, 1970482232512675841, '菜单管理', 'menu', 'system/menu/index', 2, 3, 'system:menu:list', 'tree-table', 1, 1974016655841509377, '2025-10-04 14:55:44', '2025-10-04 14:55:50', 1, 1);
INSERT INTO `tb_menu` VALUES (1974446802340073472, 1970482232512675841, '角色管理', 'role', 'system/role/index', 2, 2, 'system:role:list', 'peoples', 1, 1974016655841509377, '2025-10-04 20:09:35', '2025-10-04 20:09:35', 1, 1);
INSERT INTO `tb_menu` VALUES (1974798888420397056, 1970482232512675841, '字典管理', 'dict', 'system/dict/index', 2, 4, 'system:dict:list', 'dict', 1, 1974016655841509377, '2025-10-05 19:28:37', '2025-10-05 19:28:37', 1, 1);
INSERT INTO `tb_menu` VALUES (1974813105718456320, 0, '开发工具', 'admin/tool', NULL, 1, 2, NULL, 'tool', 1, 1974016655841509377, '2025-10-05 20:25:09', '2025-10-05 20:25:09', 1, 1);
INSERT INTO `tb_menu` VALUES (1974813383091974144, 1974813105718456320, '代码生成', 'generate', 'tool/generate/index', 2, 1, NULL, 'code', 1, 1974016655841509377, '2025-10-05 20:26:15', '2025-10-05 20:26:15', 1, 1);
INSERT INTO `tb_menu` VALUES (1975545145199874048, 1970482232512675841, '文件管理', 'file', 'system/file/index', 2, 5, 'system:file:list', 'documentation', 1, 1974016655841509377, '2025-10-07 20:54:01', '2025-10-07 20:54:01', 1, 1);
INSERT INTO `tb_menu` VALUES (1999677786563969024, 1970482232512695842, '用户查询', '', NULL, 3, 1, 'system:user:query', NULL, 1, 1974016655841509377, '2025-12-13 11:08:31', '2025-12-13 11:08:31', 1, 1);
INSERT INTO `tb_menu` VALUES (1999677973495709696, 1970482232512695842, '用户新增', NULL, NULL, 3, 2, 'system:user:add', NULL, 1, 1974016655841509377, '2025-12-13 11:09:16', '2025-12-13 11:09:16', 1, 1);
INSERT INTO `tb_menu` VALUES (1999678062322679808, 1970482232512695842, '用户修改', NULL, NULL, 3, 3, 'system:user:edit', NULL, 1, 1974016655841509377, '2025-12-13 11:09:37', '2025-12-13 11:09:37', 1, 1);
INSERT INTO `tb_menu` VALUES (1999678153087418368, 1970482232512695842, '用户删除', NULL, NULL, 3, 4, 'system:user:delete', NULL, 1, 1974016655841509377, '2025-12-13 11:09:59', '2025-12-13 11:09:59', 1, 1);
INSERT INTO `tb_menu` VALUES (1999678622434230272, 1974446802340073472, '角色查询', NULL, NULL, 3, 1, 'system:role:query', NULL, 1, 1974016655841509377, '2025-12-13 11:11:50', '2025-12-13 11:11:50', 1, 1);
INSERT INTO `tb_menu` VALUES (1999679008444416000, 1974446802340073472, '角色新增', NULL, NULL, 3, 2, 'system:role:add', NULL, 1, 1974016655841509377, '2025-12-13 11:13:23', '2025-12-13 11:13:23', 1, 1);
INSERT INTO `tb_menu` VALUES (1999679653939412992, 1974446802340073472, '角色编辑', NULL, NULL, 3, 3, 'system:role:edit', NULL, 1, 1974016655841509377, '2025-12-13 11:15:56', '2025-12-13 11:15:56', 1, 1);
INSERT INTO `tb_menu` VALUES (1999679797128757248, 1974446802340073472, '角色删除', NULL, NULL, 3, 4, 'system:role:delete', '', 1, 1974016655841509377, '2025-12-13 11:16:31', '2025-12-13 11:16:31', 1, 1);
INSERT INTO `tb_menu` VALUES (1999679925021474816, 1970482232512695843, '菜单查询', NULL, NULL, 3, 1, 'system:menu:query', '', 1, 1974016655841509377, '2025-12-13 11:17:01', '2025-12-13 11:17:01', 1, 1);
INSERT INTO `tb_menu` VALUES (1999679987575324672, 1970482232512695843, '菜单新增', NULL, NULL, 3, 2, 'system:menu:add', '', 1, 1974016655841509377, '2025-12-13 11:17:16', '2025-12-13 11:17:16', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680067585867776, 1970482232512695843, '菜单编辑', NULL, NULL, 3, 3, 'system:menu:edit', '', 1, 1974016655841509377, '2025-12-13 11:17:35', '2025-12-13 11:17:35', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680124603236352, 1970482232512695843, '菜单删除', NULL, NULL, 3, 4, 'system:menu:delete', '', 1, 1974016655841509377, '2025-12-13 11:17:49', '2025-12-13 11:17:49', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680269625491456, 1974798888420397056, '字典查询', NULL, NULL, 3, 1, 'system:dict:query', '', 1, 1974016655841509377, '2025-12-13 11:18:23', '2025-12-13 11:18:23', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680346385448960, 1974798888420397056, '字典新增', NULL, NULL, 3, 2, 'system:dict:add', '', 1, 1974016655841509377, '2025-12-13 11:18:41', '2025-12-13 11:18:41', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680429260701696, 1974798888420397056, '字典编辑', NULL, NULL, 3, 3, 'system:dict:edit', '', 1, 1974016655841509377, '2025-12-13 11:19:01', '2025-12-13 11:19:01', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680514396684288, 1974798888420397056, '字典删除', NULL, NULL, 3, 4, 'system:dict:delete', '', 1, 1974016655841509377, '2025-12-13 11:19:22', '2025-12-13 11:19:22', 1, 1);
INSERT INTO `tb_menu` VALUES (1999680600933564416, 1975545145199874048, '文件查询', NULL, NULL, 3, 1, 'system:file:query', '', 1, 1974016655841509377, '2025-12-13 11:19:42', '2025-12-13 11:19:42', 1, 1);
INSERT INTO `tb_menu` VALUES (1999681733492113408, 1975545145199874048, '文件新增', NULL, NULL, 3, 2, 'system:file:add', '', 1, 1974016655841509377, '2025-12-13 11:24:12', '2025-12-13 11:24:12', 1, 1);
INSERT INTO `tb_menu` VALUES (1999681790949883904, 1975545145199874048, '文件编辑', NULL, NULL, 3, 3, 'system:file:edit', '', 1, 1974016655841509377, '2025-12-13 11:24:26', '2025-12-13 11:24:26', 1, 1);
INSERT INTO `tb_menu` VALUES (1999681851935064064, 1975545145199874048, '文件删除', NULL, NULL, 3, 4, 'system:file:delete', '', 1, 1974016655841509377, '2025-12-13 11:24:40', '2025-12-13 11:24:40', 1, 1);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色Key',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1970482232512675840, '超级管理员', 'super_admin', 1974016655841509377, '2025-10-03 22:05:15', '2025-10-03 22:05:18', 1, 1);
INSERT INTO `tb_role` VALUES (1981350113852973056, '普通用户', 'common_user', 1974016655841509377, '2025-10-23 21:20:54', '2025-10-23 21:20:54', 1, 1);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1970482232512675841);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1970482232512695842);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1970482232512695843);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1974446802340073472);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1974798888420397056);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1974813105718456320);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1974813383091974144);
INSERT INTO `tb_role_menu` VALUES (1970482232512675840, 1975545145199874048);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1970482232512675841);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1970482232512695842);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1970482232512695843);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1974446802340073472);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1974798888420397056);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1975545145199874048);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999677786563969024);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999677973495709696);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999678062322679808);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999678153087418368);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999678622434230272);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999679008444416000);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999679653939412992);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999679797128757248);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999679925021474816);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999679987575324672);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680067585867776);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680124603236352);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680269625491456);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680346385448960);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680429260701696);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680514396684288);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999680600933564416);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999681733492113408);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999681790949883904);
INSERT INTO `tb_role_menu` VALUES (1981350113852973056, 1999681851935064064);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账户',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `ver` int NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1974016655841509377, 'admin', '$2a$10$ixVpCBU00FIfut/ZiUq/yuuuA837rvPibpU2xYxxHqnLxRZgKSrkW', 'http://localhost:15000/system/oss/download/1975799014760820736', '管理员', NULL, '2025-10-03 15:40:21', '2025-10-03 15:40:21', 1, 1);
INSERT INTO `tb_user` VALUES (1974016655841509378, '123456', '$2a$10$ixVpCBU00FIfut/ZiUq/yuuuA837rvPibpU2xYxxHqnLxRZgKSrkW', 'http://localhost:15000/system/oss/download/1975799014760820736', '用户9zig9', NULL, '2025-10-03 15:40:21', '2025-10-03 15:40:21', 1, 1);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1974016655841509377, 1970482232512675840);
INSERT INTO `tb_user_role` VALUES (1974016655841509378, 1981350113852973056);

SET FOREIGN_KEY_CHECKS = 1;
