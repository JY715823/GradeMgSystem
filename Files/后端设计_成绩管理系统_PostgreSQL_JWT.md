# 后端设计文档（Spring Boot + PostgreSQL + Token/JWT）——贴合《简易成绩管理系统》规划

> 目的：给前后端联调提供统一且可落地的后端设计（数据表、接口、权限、核心规则）。  
> 贴合范围：三角色（学生/教师/管理员）、开课/排课（Offering）、选课（Enrollment）、成绩草稿/发布（Grade）、忘记密码=管理员重置申请闭环、个人中心改信息+改密码。  

---

## 1. 技术栈与工程结构

### 1.1 技术选型
- Spring Boot 3.x
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- PostgreSQL
- Bean Validation（JSR-380）
- Token 认证：推荐 JWT

### 1.2 分层（推荐）
- controller：REST API（只做参数校验、鉴权、返回 DTO）
- service：业务规则（容量/冲突/发布逻辑/重置密码）
- repository：JPA Repository
- entity：数据库实体
- dto：请求/响应模型
- security：JWT 过滤器、权限配置
- common：统一返回、异常处理、工具类

---

## 2. JWT 是什么？为什么推荐用它

JWT（JSON Web Token）是一种自包含的 Token 格式：
- 登录成功后服务端签发一个字符串 token（JWT）
- 前端把它放到 Authorization: Bearer <token> 里带回请求
- 后端验证签名和过期时间，就能知道“是谁、是什么角色”

优点：
- 无状态（不需要服务端保存 session）
- 实现快，适合课程项目
- Token 内可携带 userId、role 等（但不要放敏感信息）

---

## 3. 权限模型（RBAC）

- ROLE_STUDENT
- ROLE_TEACHER
- ROLE_ADMIN

授权建议：
- Controller 层用 @PreAuthorize
- Service 层做数据归属校验（教师只能访问自己的 offeringId）

---

## 4. 数据模型（PostgreSQL 表设计）

关键约束：
- Enrollment：(offering_id, student_id) 唯一
- Grade：(offering_id, student_id) 唯一
- UserAccount：username 唯一

### 4.1 建表 SQL

```sql
create table if not exists user_account (
  id bigserial primary key,
  role varchar(16) not null,
  username varchar(64) not null unique,
  password_hash varchar(255) not null,
  profile_type varchar(16) not null,
  profile_id bigint not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists student (
  id bigserial primary key,
  student_no varchar(64) not null unique,
  name varchar(64) not null,
  class_name varchar(64) not null,
  phone varchar(32),
  email varchar(128),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists teacher (
  id bigserial primary key,
  teacher_no varchar(64) not null unique,
  name varchar(64) not null,
  dept varchar(128),
  phone varchar(32),
  email varchar(128),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists course (
  id bigserial primary key,
  course_code varchar(64) not null unique,
  name varchar(128) not null,
  credit numeric(4,1),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists offering (
  id bigserial primary key,
  term varchar(32) not null,
  course_id bigint not null references course(id),
  teacher_id bigint not null references teacher(id),
  class_label varchar(64),
  weekday smallint not null,
  start_period smallint not null,
  end_period smallint not null,
  start_week smallint not null,
  end_week smallint not null,
  location varchar(128),
  capacity int not null,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists enrollment (
  id bigserial primary key,
  offering_id bigint not null references offering(id) on delete cascade,
  student_id bigint not null references student(id) on delete cascade,
  created_at timestamptz not null default now(),
  unique (offering_id, student_id)
);

create table if not exists grade (
  id bigserial primary key,
  offering_id bigint not null references offering(id) on delete cascade,
  student_id bigint not null references student(id) on delete cascade,
  score numeric(5,2),
  status varchar(16) not null default 'DRAFT',
  updated_at timestamptz not null default now(),
  unique (offering_id, student_id)
);

create table if not exists password_reset_request (
  id bigserial primary key,
  role varchar(16) not null,
  username varchar(64) not null,
  name varchar(64) not null,
  remark varchar(255),
  status varchar(16) not null default 'PENDING',
  created_at timestamptz not null default now(),
  handled_at timestamptz,
  handled_by bigint references user_account(id)
);
```

---

## 5. 核心业务规则（Service 层实现点）

### 5.1 选课容量与冲突校验
冲突条件（与已选 offering 比较）：
- term 相同
- weekday 相同
- 节次区间重叠：max(startPeriod) <= min(endPeriod)
- 周次区间重叠：max(startWeek) <= min(endWeek)

容量：enrollment.count(offeringId) < capacity

### 5.2 成绩草稿/发布
- 保存草稿：upsert grade，status=DRAFT
- 发布：该 offering 下所有 grade.status=PUBLISHED
- 学生查成绩：仅返回 PUBLISHED；或 DRAFT score 置空

### 5.3 忘记密码（管理员重置闭环）
- 提交申请：插入 password_reset_request(status=PENDING)
- 管理员处理：生成新密码，更新 user_account.password_hash，标记 DONE

### 5.4 个人中心
- 修改信息：phone/email
- 改密码：校验旧密码后 BCrypt 更新

---

## 6. 统一返回与错误码规范

响应：
```json
{ "code": 0, "message": "ok", "data": {} }
```

建议错误码：
- 4010 Token 无效
- 4030 无权限
- 4092 选课重复
- 4093 容量已满
- 4094 时间冲突

---

## 7. REST API 设计（与页面贴合）

统一前缀：/api  
鉴权：除 /api/auth/** 与 /api/password-resets/request 外均需 Bearer token

### 7.1 登录
POST /api/auth/login
```json
{ "role": "STUDENT", "username": "20231234", "password": "123456" }
```

### 7.2 忘记密码申请（公开）
POST /api/password-resets/request
```json
{ "role":"STUDENT","username":"20231234","name":"张三","remark":"忘记了" }
```

### 7.3 个人中心
GET /api/profile/me  
PUT /api/profile/me  
POST /api/profile/change-password

### 7.4 学生端
GET /api/student/offerings?term=&keyword=  
POST /api/student/enrollments  { "offeringId": 10 }  
DELETE /api/student/enrollments/{offeringId}  
GET /api/student/timetable?term=  
GET /api/student/grades?term=

### 7.5 教师端
GET /api/teacher/offerings?term=  
GET /api/teacher/offerings/{offeringId}/students  
PUT /api/teacher/offerings/{offeringId}/grades/draft  
POST /api/teacher/offerings/{offeringId}/grades/publish  

### 7.6 管理员端
学生 CRUD：
- GET/POST/PUT/DELETE /api/admin/students
- POST /api/admin/students/{id}/reset-password

教师 CRUD：
- GET/POST/PUT/DELETE /api/admin/teachers
- POST /api/admin/teachers/{id}/reset-password

课程 CRUD：
- GET/POST/PUT/DELETE /api/admin/courses

开课 CRUD：
- GET/POST/PUT/DELETE /api/admin/offerings

选课信息：
- GET /api/admin/offerings/{offeringId}/enrollments
- DELETE /api/admin/offerings/{offeringId}/enrollments/{studentId}

重置申请：
- GET /api/admin/password-resets?status=PENDING
- POST /api/admin/password-resets/{id}/handle

