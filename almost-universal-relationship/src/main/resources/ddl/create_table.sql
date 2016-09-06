CREATE TABLE `t_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '系统内部ID',
  `created_by_id` bigint(20) unsigned DEFAULT '0' COMMENT '创建账号',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by_id` bigint(20) unsigned DEFAULT '0' COMMENT ' 更新账号',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `version` bigint(20) unsigned DEFAULT '0' COMMENT '版本号',
  `status` int(11) unsigned DEFAULT '1' COMMENT '状态',
  `object_id` bigint(20) DEFAULT NULL COMMENT '主体id',
  `object_type` int(11) DEFAULT NULL COMMENT '主体type',
  `related` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联属性',
  `clazz` varchar(500) DEFAULT NULL COMMENT '关联属性的java class',
  `related_type` int(11) DEFAULT NULL COMMENT '关联对象',
  PRIMARY KEY (`id`),
  KEY `idx_objectId_status_relatedType` (`object_id`,`status`,`related_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `t_relation_remove` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '系统内部ID',
  `created_by_id` bigint(20) unsigned DEFAULT '0' COMMENT '创建账号',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by_id` bigint(20) unsigned DEFAULT '0' COMMENT ' 更新账号',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `version` bigint(20) unsigned DEFAULT '0' COMMENT '版本号',
  `status` int(11) unsigned DEFAULT '0' COMMENT '状态',
  `object_id` bigint(20) DEFAULT NULL COMMENT '主体id',
  `object_type` int(11) DEFAULT NULL COMMENT '主体type',
  `related` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联属性',
  `clazz` varchar(500) DEFAULT NULL COMMENT '关联属性的java class',
  `related_type` int(11) DEFAULT NULL COMMENT '关联对象',
  PRIMARY KEY (`id`),
  KEY `idx_objectId_relatedType` (`object_id`,`related_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;