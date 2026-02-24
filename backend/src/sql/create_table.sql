create table if not exists user
(
    id               bigint auto_increment comment 'id' primary key,
    userAccount      varchar(256)                           not null comment '账号',
    userPassword     varchar(512)                           not null comment '密码',
    userName         varchar(256)                           null comment '用户昵称',
    userAvatar       varchar(1024)                          null comment '用户头像',
    userProfile      varchar(512)                           null comment '用户简介',
    userRole         varchar(256) default 'user'            not null comment '用户角色：user/admin',
    editTime         datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime       datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint      default 0                 not null comment '是否删除',
    subscriptionPlan varchar(256) DEFAULT 'free' COMMENT '订阅计划:free/basic/pro/enterprise',
    aiCredits        INT          DEFAULT 100 COMMENT 'AI积分',
    lastLoginTime    TIMESTAMP                              NULL COMMENT '最后登录时间',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 项目主表
create table if not exists  project
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID，主键',
    projectName        VARCHAR(255) NOT NULL COMMENT '项目名称',
    projectDescription TEXT COMMENT '项目描述',
    projectType        VARCHAR(50) COMMENT '项目类型（如：短视频、广告、宣传片等）',
    projectStatus      VARCHAR(50)   DEFAULT 'draft' COMMENT '项目状态: draft(草稿)/progress(进行中)/completed(已完成)',

    userId             BIGINT       NOT NULL COMMENT '创建用户ID',
    coverUrl           VARCHAR(500) COMMENT '封面图URL',

    shotCount          INT           DEFAULT 0 COMMENT '分镜数量',
    totalDuration      INT COMMENT '总时长（秒）',
    progressRate       DECIMAL(5, 2) DEFAULT 0.00 COMMENT '完成进度百分比（0-100）',

    lastShotIndex      INT           DEFAULT 0 COMMENT '最后操作的分镜序号',
    currentVersion     INT           DEFAULT 1 COMMENT '当前版本号',
    maxVersion         INT           DEFAULT 1 COMMENT '最大版本号',
    shotOrder          JSON NOT NULL DEFAULT '[]' comment '分镜顺序数组',

    isTemplate         TINYINT(1)    DEFAULT 0 COMMENT '是否为模板（0:否, 1:是）',
    isDeleted          TINYINT(1)    DEFAULT 0 COMMENT '软删除标记（0:正常, 1:已删除）',
    isPublic           TINYINT(1)    DEFAULT 0 COMMENT '是否公开（0:私有, 1:公开）',

    createTime         DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime         DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleteTime         DATETIME COMMENT '删除时间',

    INDEX idx_userId (userId) COMMENT '用户ID索引',
    INDEX idx_projectStatus (projectStatus) COMMENT '项目状态索引',
    INDEX idx_createTime (createTime) COMMENT '创建时间索引',
    INDEX idx_projectType (projectType) COMMENT '项目类型索引',
    INDEX idx_isTemplate (isTemplate) COMMENT '模板标记索引',
    INDEX idx_isDeleted (isDeleted) COMMENT '删除标记索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='项目表';

-- 分镜主表
create table if not exists shot
(
    shotId          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分镜ID，主键',
    projectId       BIGINT       NOT NULL COMMENT '所属项目ID',

    shotTitle       VARCHAR(255) NOT NULL COMMENT '分镜标题',
    shotDesc        TEXT COMMENT '分镜描述',
    duration        INT         DEFAULT 0 COMMENT '分镜时长（秒）',
    orderIndex      INT         DEFAULT 0 COMMENT '排序序号（用于拖拽排序）',
    shotStatus      VARCHAR(50) DEFAULT 'draft' COMMENT '分镜状态：draft(草稿)/inProgress(进行中)/completed(已完成)',

    scriptContent   LONGTEXT COMMENT '分镜对应剧本内容',
    isDeleted       TINYINT(1)  DEFAULT 0 COMMENT '软删除标记（0:正常, 1:已删除）',

    createTime      DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime      DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleteTime      DATETIME COMMENT '删除时间',

    INDEX idx_projectId (projectId) COMMENT '项目ID索引',
    INDEX idx_orderIndex (orderIndex) COMMENT '排序序号索引',
    INDEX idx_shotStatus (shotStatus) COMMENT '分镜状态索引',
    INDEX idx_isDeleted (isDeleted) COMMENT '删除标记索引',
    UNIQUE KEY uk_project_shot_number (projectId, orderIndex) COMMENT '项目内分镜编号唯一约束'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分镜主表';

-- 分镜素材关联表
create table if not exists shot_material
(
    materialId      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '素材ID',
    shotId          BIGINT NOT NULL COMMENT '分镜ID',
    materialType    VARCHAR(50) COMMENT '素材类型：image(图片)/video(视频)/audio(音频)',
    materialRole    VARCHAR(50) COMMENT '素材角色：background(背景)/character(角色)/prop(道具)/audio(音效)',
    materialDesc    VARCHAR(512) comment '素材描述',

    isDeleted       TINYINT(1) DEFAULT 0 COMMENT '软删除标记（0:正常, 1:已删除）',
    createTime      DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime      DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_shotId (shotId) COMMENT '分镜ID索引',
    INDEX idx_materialId (materialId) COMMENT '素材ID索引',
    INDEX idx_materialType (materialType) COMMENT '素材类型索引',
    UNIQUE KEY uk_shot_material (shotId, materialId, materialRole) COMMENT '分镜-素材-角色唯一约束'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分镜素材关联表';
