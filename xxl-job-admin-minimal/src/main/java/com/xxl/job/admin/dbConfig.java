package com.xxl.job.admin;

public class dbConfig {
    public static String getConfigJson() {
        String configJson;
        configJson = "{\n" +
                "  \"serverPort\": \"9001\",\n" +
                "  \"executorLogPath\":\"./log/\",\n" +
                "  \"executorAddresses\":\"http://127.0.0.1:9001/xxl-job-admin\",\n" +
                "  \"executorPort\":\"9998\",\n" +
                "  \"MariaDB4j\": \"true\",\n" +
                "  \"mysqlUrl\": \"jdbc:mysql://localhost:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai\",\n" +
                "  \"mysqlPort\":\"3306\",\n" +
                "  \"mysqlUserName\":\"root\",\n" +
                "  \"mysqlPassword\":\"\",\n" +
                "  \"mysqlDataPath\": \"./mysqlData\"\n" +
                "}";
        return configJson;
    }

    public static String getDbSQL() {
        String dbSQL;
        dbSQL = "CREATE TABLE `xxl_job_info` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',\n" +
                "  `job_desc` varchar(255) NOT NULL,\n" +
                "  `add_time` datetime DEFAULT NULL,\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  `author` varchar(64) DEFAULT NULL COMMENT '作者',\n" +
                "  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',\n" +
                "  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',\n" +
                "  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',\n" +
                "  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',\n" +
                "  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',\n" +
                "  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',\n" +
                "  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',\n" +
                "  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',\n" +
                "  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',\n" +
                "  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',\n" +
                "  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',\n" +
                "  `glue_source` mediumtext COMMENT 'GLUE源代码',\n" +
                "  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',\n" +
                "  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',\n" +
                "  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',\n" +
                "  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',\n" +
                "  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',\n" +
                "  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_log` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',\n" +
                "  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',\n" +
                "  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',\n" +
                "  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',\n" +
                "  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',\n" +
                "  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',\n" +
                "  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',\n" +
                "  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',\n" +
                "  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',\n" +
                "  `trigger_msg` text COMMENT '调度-日志',\n" +
                "  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',\n" +
                "  `handle_code` int(11) NOT NULL COMMENT '执行-状态',\n" +
                "  `handle_msg` text COMMENT '执行-日志',\n" +
                "  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `I_trigger_time` (`trigger_time`),\n" +
                "  KEY `I_handle_code` (`handle_code`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_log_report` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',\n" +
                "  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',\n" +
                "  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',\n" +
                "  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_logglue` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',\n" +
                "  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',\n" +
                "  `glue_source` mediumtext COMMENT 'GLUE源代码',\n" +
                "  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',\n" +
                "  `add_time` datetime DEFAULT NULL,\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_registry` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `registry_group` varchar(50) NOT NULL,\n" +
                "  `registry_key` varchar(255) NOT NULL,\n" +
                "  `registry_value` varchar(255) NOT NULL,\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_group` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',\n" +
                "  `title` varchar(12) NOT NULL COMMENT '执行器名称',\n" +
                "  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',\n" +
                "  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',\n" +
                "  `update_time` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_user` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `username` varchar(50) NOT NULL COMMENT '账号',\n" +
                "  `password` varchar(50) NOT NULL COMMENT '密码',\n" +
                "  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',\n" +
                "  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `i_username` (`username`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "CREATE TABLE `xxl_job_lock` (\n" +
                "  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',\n" +
                "  PRIMARY KEY (`lock_name`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n" +
                "\n" +
                "INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (1, 'xxl-job-executor', '任务执行器', 0, NULL, '2018-11-03 22:21:31' );\n" +
                "INSERT INTO `xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, 1, '测试任务1', '2018-11-03 22:21:31', '2018-11-03 22:21:31', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '');\n" +
                "INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);\n" +
                "INSERT INTO `xxl_job_lock` ( `lock_name`) VALUES ( 'schedule_lock');\n" +
                "\n" +
                "commit;\n" +
                "\n";
        return dbSQL;
    }
}
