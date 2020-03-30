create schema if not exists `asr-aimark` collate utf8mb4_0900_ai_ci;

create table if not exists action
(
    ActionId int not null comment '操作编号'
        primary key,
    ActionName varchar(255) not null comment '操作名称',
    ActionType int not null comment '操作类型, 1=菜单显示, 2=操作权限',
    Level int not null comment '操作级别, 1=一级菜单, 2=二级菜单',
    ParentActionId int not null comment '父级操作编号, 用于多级菜单的控制',
    DeleteFlag bit default b'0' not null comment '删除标记, 0=正常, 1=删除',
    RowVersion int default 1 not null comment '数据行版本, 每次更新+1'
)
    collate=utf8mb4_unicode_ci;

create table if not exists role
(
    RoleId int not null comment '角色编号'
        primary key,
    RoleName varchar(255) not null comment '角色名称',
    DeleteFlag bit default b'0' not null comment '删除标记, 0=正常, 1=删除',
    RowVersion int default 1 not null comment '数据行版本, 每次更新+1'
)
    collate=utf8mb4_unicode_ci;

create table if not exists roleaction
(
    RoleId int not null,
    ActionId int not null,
    primary key (RoleId, ActionId)
)
    collate=utf8mb4_unicode_ci;

create table if not exists token
(
    UserId varchar(50) not null
        primary key,
    AccessToken varchar(255) null,
    RefreshToken varchar(255) null,
    DeleteFlag bit default b'0' not null comment '删除标记, 0=正常, 1=删除',
    RowVersion int default 1 not null comment '数据行版本, 每次更新+1'
)
    collate=utf8mb4_unicode_ci;

create table if not exists user
(
    Id int auto_increment comment '用户编号'
        primary key,
    Name varchar(50) null comment '用户名称, 用于显示',
    LoginName varchar(50) not null comment '用户登录名, 用于登录',
    PasswordHash varchar(255) null comment '密码, hash后字符串',
    Mobile varchar(50) null comment '手机号码',
    Email varchar(50) null comment '邮箱',
    Status int default 0 not null comment '状态, 0=正常, 1=停用',
    DeleteFlag bit default b'0' not null comment '删除标记, 0=正常, 1=删除',
    RowVersion int default 1 not null comment '数据行版本, 每次更新+1'
)
    collate=utf8mb4_unicode_ci;

create table if not exists userLoginHistory
(
    UserId int not null comment '用户编号',
    IP varchar(50) not null comment '用户ip地址',
    LastLoginDateTime datetime not null comment '上次登录时间',
    LoginSuccess tinyint(1) default 0 not null comment '是否登录成功',
    LoginFailedCount int default 0 not null comment '登录失败累计的次数',
    primary key (UserId, IP)
)
    comment '每次用户登录时, 记录相关信息.每次用户登录时, 记录相关信息.  相同用户名+IP 只记录一条记录';

create table if not exists userrole
(
    UserId int not null comment '用户编号',
    RoleId int not null comment '角色编号',
    primary key (UserId, RoleId)
)
    collate=utf8mb4_unicode_ci;

