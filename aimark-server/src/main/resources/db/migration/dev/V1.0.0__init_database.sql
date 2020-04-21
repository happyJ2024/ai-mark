-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
                         `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '项目名称',
                         `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '项目状态：0-未完成，1-已完成',
                         `is_deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                         `row_version` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '数据版本',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

