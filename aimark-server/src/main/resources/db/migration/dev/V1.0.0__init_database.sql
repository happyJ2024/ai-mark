-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '项目名称',
  `status` int(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目状态：0-未完成，1-已完成',
  `is_deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `row_version` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for source
-- ----------------------------
DROP TABLE IF EXISTS `source`;
CREATE TABLE `source`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '对应的项目ID',
  `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原始文件全名',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件类型',
  `url_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'url相对路径',
  `is_converted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已转换图片',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sou_uk_1`(`item_id`, `origin_name`) USING BTREE COMMENT '同一项目下文件名唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for text_image
-- ----------------------------
DROP TABLE IF EXISTS `text_image`;
CREATE TABLE `text_image`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '对应项目ID',
  `source_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '对应源文件ID',
  `image_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片名称',
  `page_index` int(11) UNSIGNED NOT NULL COMMENT '图片对应源文件页数',
  `url_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'url相对路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

