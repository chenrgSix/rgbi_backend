# 数据库初始化

-- 创建库
create database if not exists rg_intelligent;

-- 切换库
use rg_intelligent;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;
-- 图表表
create table if not exists chart
(
    id          bigint auto_increment comment 'id' primary key,
    goal        text                               null comment '分析目标',
    `name`      varchar(128)                       null comment '图表名称',
    chartData   text                               null comment '图表数据',
    chartType   varchar(128)                       null comment '图表类型',
    genChart    text                               null comment '生成的图表数据',
    genResult   text                               null comment '生成的分析结论',
    status      varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage text                               null comment '执行信息',
    userId      bigint                             null comment '创建用户id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;

# 积分
CREATE TABLE score (
   id  bigint auto_increment comment 'id' primary key,
   userId bigint(20) NOT NULL,
   scoreTotal bigint(20) NOT NULL,
   isSign tinyint(4) NOT NULL DEFAULT '0' COMMENT '0表示未签到，1表示已签到',
   createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
       isDelete    tinyint  default 0                 not null comment '是否删除'
)  comment'积分表' collate = utf8mb4_unicode_ci;
# chat聊天对话表

CREATE TABLE dialogues(
     id  bigint auto_increment comment 'id' primary key, -- 会话id
     userId bigint(20) NOT NULL comment '用户id',  -- 用户id
     chatContent text NULL comment '聊天内容',  -- 聊天内容
     chatTitle varchar(128) NOT NULL comment '聊天主题', -- 聊天主题
     kbIds JSON NULL comment '知识库id集合', -- 知识库id集合
     createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
     updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
     isDelete    tinyint  default 0                 not null comment '是否删除'
)  comment'对话表' collate = utf8mb4_unicode_ci;

# ai模型表--管理员才添加更改
CREATE TABLE ai_model(
     id  bigint auto_increment comment 'id' primary key,
     userId bigint(20) NOT NULL  comment '用户id', -- 当前主要记录是哪个管理员添加的
     name varchar(64) NULL comment '模型名称',
     type varchar(64) NULL comment '类别', -- 方便区分文本、图片、多模态
     setting varchar(512) NULL comment '配置',
     remark varchar(512) NULL comment '备注',
     platform varchar(45) NULL comment '平台',
     maxInputTokens  int           default 0 comment '最大输入token',
     maxOutputTokens int           default 0 comment '最大输出token',
     isFree TINYINT(1) NULL comment '是否免费',
     isEnable TINYINT(1) NULL comment '是否启用',
     createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
     updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
     isDelete    tinyint  default 0                 not null comment '是否删除'
)  comment'模型表' collate = utf8mb4_unicode_ci;

#
CREATE TABLE sys_config
(
    id  bigint auto_increment comment 'id' primary key,
    name        varchar(100)  default ''             not null comment '配置项名称',
    value       varchar(1000) default ''             not null comment '配置项值',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)comment'系统配置表' collate = utf8mb4_unicode_ci;

CREATE TABLE file_upload
(
    id  bigint auto_increment comment 'id' primary key,
    userId bigint(20) NOT NULL  comment '用户id',
    fileName            varchar(255) not null comment '文件名，实际存储此文件的名称，带后缀',
    displayName         varchar(255) not null comment '原始名称，不带后缀',
    fileSuffix          varchar(32) comment '文件的类型（后缀）',
    fileSize            bigint default 0,
    path                 varchar(255) not null comment '文件的路径',
    fileDesc            varchar(255) default null,
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)comment'文件表' collate = utf8mb4_unicode_ci;

CREATE TABLE knowledge_base
(
    id  bigint auto_increment comment 'id' primary key,
    userId  bigint(20) NOT NULL COMMENT '用户ID',
    title VARCHAR(250) NOT NULL COMMENT '标题',
    remark TEXT NULL COMMENT '备注',
    isPublic TINYINT(1) DEFAULT 0 NOT NULL COMMENT '是否公开，0：否；1：是',
    docNum INT DEFAULT 0 NOT NULL COMMENT '文档数量',
    ingestMaxSegment INT DEFAULT 1024 NOT NULL COMMENT '最大分割大小',
    ingestMaxOverlap INT DEFAULT 0 NOT NULL COMMENT '最大重叠大小',
    ingestModelName VARCHAR(45) DEFAULT '' NOT NULL COMMENT '模型名称',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) COMMENT='知识库';

CREATE TABLE knowledge_document (
   id  bigint auto_increment comment 'id' primary key,
   kbId  bigint(20) NOT NULL COMMENT '知识库ID',
   userId  bigint(20) NOT NULL COMMENT '用户ID',
   fileId  bigint(20) NULL COMMENT '文件ID',
   docType VARCHAR(255) NOT NULL COMMENT '文档类型',
   status tinyint default 0  NOT NULL COMMENT '文档状态 -1：解析失败 0：未解析 1：解析中 2：解析完成',
   createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
   updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
   isDelete    tinyint  default 0                 not null comment '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识文档';
# 数据初始化
INSERT INTO sys_config (id, name, value, createTime, updateTime, isDelete) VALUES (1, 'openai_setting', '{"secret_key":""}', '2025-03-09 07:46:01', '2025-03-09 07:46:01', 0);
INSERT INTO sys_config (id, name, value, createTime, updateTime, isDelete) VALUES (2, 'deepseek_setting', '{"base_url":"https://api.deepseek.com","secret_key":""}', '2025-03-09 07:46:24', '2025-03-10 13:35:15', 0);
INSERT INTO sys_config (id, name, value, createTime, updateTime, isDelete) VALUES (3, 'ollama_setting', '"{base_url": ""}', '2025-03-09 07:46:37', '2025-04-08 12:47:53', 0);
INSERT INTO sys_config (id, name, value, createTime, updateTime, isDelete) VALUES (4, 'zhipu_setting', '{"secret_key": ""}', '2025-03-09 15:47:10', '2025-03-09 07:47:17', 0);
INSERT INTO user (id, userAccount, userPassword, userName, userAvatar, userRole, createTime, updateTime, isDelete) VALUES (1890713823228264450, 'rgadmin', '517e6f3f94ea33012dae009d7ecca3f3', 'rgadmin', '', 'admin', '2025-02-15 18:44:37', '2025-03-08 06:22:21', 0);
INSERT INTO ai_model (id, userId, name, type, setting, remark, platform, maxInputTokens, maxOutputTokens, isFree, isEnable, createTime, updateTime, isDelete) VALUES (1, 1890713823228264450, 'GLM-4-Flash', 'text', '', '智普免费ai', 'zhipu', 61440, 4096, 1, 1, '2025-03-09 05:53:46', '2025-03-09 08:31:31', 0);

