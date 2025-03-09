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
)comment'系统配置表' collate = utf8mb4_unicode_ci;;