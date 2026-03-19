USE madgod;
CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
    `no` varchar(20) UNIQUE COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
    `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名字',
    `password` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `age` int DEFAULT NULL,
    `sex` int DEFAULT NULL COMMENT '性别',
    `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
    `role_id` int DEFAULT NULL COMMENT '角色 0超级管理员，1管理员，2普通账号',
    `isValid` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT 'Y' COMMENT '是否有效，Y有效，其他无效',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci