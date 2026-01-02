# 开发规范文档（Spring Boot + Vue3/Vite）——简易成绩管理系统

> 目的：在 5 人协作开发中统一工程结构、接口约定、代码风格与页面视觉样式，避免“能跑但不一致、难合并、难答辩”。

---

## 1. 仓库与目录规范（必须遵守）

### 1.1 仓库结构（Monorepo）
```
grade-system/
  backend/          # Spring Boot（IntelliJ IDEA 打开该目录）
  frontend/         # Vue3/Vite（VS Code 打开该目录）
  docs/             # 规划/接口/规范文档
  README.md
```

### 1.2 重要约定
- **只使用一个主分支 `main`** 作为可演示版本；任何功能开发必须走功能分支后合并回 main。
- 任何影响前后端联调的修改（DTO 字段、接口路径、状态码）必须同步更新 `docs/` 中的接口约定。

---

## 2. Git 协作规范（分支/提交/合并）

### 2.1 分支命名
- 名字-功能（eg:hsf-student）
- 创建自己的分支在自己的分支里面实现功能

### 2.2 提交信息（Commit Message）
格式：
```
<type>(<scope>): <subject>
```
type 取值：`feat | fix | refactor | chore | docs | test`  
示例：

- `feat(student): add select course page`
- `fix(api): handle 409 conflict message`
- `chore(frontend): add eslint config`

·做一个小功能就提交一次，多提交，方便改。

---

## 3. 统一接口与错误处理规范（前后端共同遵守）

### 3.1 API 基础约定
- API 前缀统一：`/api`
- 认证：`Authorization: Bearer <token>`

### 3.2 统一返回格式
```json
{ "code": 0, "message": "ok", "data": {} }
```

### 3.3 常用错误码（建议）
- `4010`：Token 无效/未登录
- `4030`：无权限
- `4092`：选课重复
- `4093`：容量已满
- `4094`：时间冲突
- `5000`：系统异常（统一兜底）

### 3.4 前端错误展示
- 业务错误（code != 0）：使用 `ElMessage.error(message)`。
- 网络错误/超时：提示“网络异常，请稍后重试”。

---

## 4. 前端工程规范（Vue3 + TS + Pinia + Router + Element Plus）

### 4.1 技术栈与写法
- Vue 3 **Composition API** + `<script setup lang="ts">`
- 必须 TypeScript：禁止大面积 `any`
- 状态：Pinia；路由：Vue Router；UI：Element Plus

### 4.2 前端目录约定（frontend/src）
```
src/
  api/                 # API 封装（按领域拆分）
  assets/
  components/          # 通用组件（统一风格）
  layouts/             # Layout（侧边栏+顶部栏）
  router/
  stores/
  styles/              # 全局样式与变量
  types/               # 类型定义（全局共享）
  utils/
  views/               # 页面（按角色/模块分组）
```

### 4.3 命名规范
- 文件夹：`kebab-case`（例：`select-courses`）
- 组件：`PascalCase`（例：`FormDialog.vue`）
- TS 类型：`PascalCase`（例：`Student`, `Offering`）
- 变量/函数：`camelCase`
- Pinia store：`useXxxStore`（例：`useAuthStore`）

### 4.4 类型定义（必须集中）
- 全局类型放 `src/types/`，禁止每个页面私自定义同名结构。
- 领域建议拆文件：
  - `types/auth.ts`
  - `types/student.ts`
  - `types/course.ts`
  - `types/offering.ts`
  - `types/grade.ts`

### 4.5 API 封装（必须统一）
- 统一 `axios` 实例：`src/api/http.ts`
  - 自动附带 token
  - 统一响应解包（code/message/data）
- 领域 API 文件示例：
  - `src/api/student.ts`
  - `src/api/teacher.ts`
  - `src/api/admin.ts`
  - `src/api/profile.ts`

### 4.6 路由与权限
- 路由守卫：未登录跳 `/login`
- 角色权限：菜单与路由都要限制（不只隐藏菜单）
- `/home` 根据 role 渲染不同首页内容

---

## 5. 页面视觉样式规范（强制，避免“五颜六色”）

> 核心原则：**统一布局、统一间距、统一组件用法**。  
> 禁止随意改主题色、随意用不同的按钮样式/字体大小。

### 5.1 全局视觉基线
- 页面背景：浅灰（例如 `#F5F7FA`），内容卡片为白色
- 主色（Primary）：Element Plus 默认蓝（不要自改主题色）
- 字体：系统默认（无需引入额外字体）
- 圆角：卡片/弹窗/输入框保持默认或统一为 8px（不混用）
- 阴影：仅卡片轻微阴影（或不加），禁止夸张阴影

### 5.2 布局与间距（统一）
- 内容区统一内边距：`24px`
- 页面标题区与内容区间距：`16px`
- 表格上方操作栏（按钮/筛选）与表格间距：`12px`
- 表单项间距：Element Plus 默认（或 `16px`）

### 5.3 页面结构模板（所有业务页统一长相）
每个业务页必须采用以下结构（建议封装 `PageContainer`）：

1. **PageHeader（页头）**
   - 左：页面标题（H2/H3）
   - 右：主要操作按钮（如“新增”）
2. **FilterBar（筛选区，可选）**
   - 简短筛选项（1 行内，超过就折叠）
3. **ContentCard（内容卡片）**
   - 表格（ElTable）+ 分页（ElPagination）

### 5.4 表格规范（统一字段排布与操作区）
- 表格最右侧固定一列“操作”，仅放：
  - 编辑（主按钮文字/链接按钮）
  - 删除（危险操作，二次确认）
  - 其他（如重置密码）放在“更多”下拉（避免按钮乱）
- 表格列宽：重要字段固定宽度，长字段省略号（tooltip 展示）
- 表格默认：斑马纹可开（统一开/关即可，全组一致）

### 5.5 表单与弹窗规范（统一）
- 新增/编辑使用 `ElDialog + ElForm`
- Dialog 标准宽度：
  - CRUD 表单：`520px`（统一）
  - 复杂表单（开课/排课）：`680px`（统一）
- 表单 label 宽度统一：`120px`
- 必填项必须使用表单校验（rules）

### 5.6 按钮规范（统一）
- 主按钮（Primary）：新增/保存/发布
- 次按钮（Default）：取消/返回/保存草稿
- 危险按钮（Danger）：删除/重置密码/移除选课（必须二次确认）
- 禁止出现多套配色按钮（不自定义 CSS 改色）

### 5.7 文案规范（统一）
- 成功提示统一：`操作成功`
- 删除提示统一：`确认删除该记录？`
- 发布成绩提示：`确认发布成绩？发布后学生可查看。`
- 表单校验提示：直白、短句（例：`请输入学号`、`分数范围 0-100`）

### 5.8 角色首页（Home）样式规范
- 统一三块内容：
  1) 欢迎卡片：`欢迎，{name}`
  2) 统计卡片（可选，不做图表也可）
- 卡片布局：2~3 列栅格（ElRow/ElCol），保持统一间距

### 5.9 Tabs 组件使用规范（教师课程详情页）
- Tabs 用 Element Plus `ElTabs`（不要自己造）
- Tab 名称统一：
  - `学生名单`
  - `成绩管理`
- Tabs 与内容区同一张卡片内（视觉统一）

---

## 6. 后端工程规范（Spring Boot + PostgreSQL）

### 6.1 分层与命名
- Controller：`XxxController`
- Service：`XxxService`
- Repository：`XxxRepository`
- DTO：`XxxRequest` / `XxxResponse`
- Entity：与表同名（如 `Offering`）

### 6.2 后端目录建议（backend/src/main/java/...）
```
com.example.gradesystem/
  common/        # ApiResponse, ErrorCode, GlobalExceptionHandler
  security/      # JwtFilter, SecurityConfig
  auth/
  admin/
  student/
  teacher/
  profile/
  entity/
  repository/
```

### 6.3 数据库与迁移
- 推荐 Flyway：`db/migration/V1__init.sql`
- 开发期也可先用 `ddl-auto=update`，但最终建议切 Flyway

### 6.4 校验与异常
- Controller 入参使用 `@Valid`
- 业务冲突用 `BusinessException(code, message)`
- 全局异常处理统一返回 `{code,message,data}`

### 6.5 事务
- 选课、发布成绩、处理重置申请：统一 `@Transactional`

### 6.6 密码安全
- 存 `BCrypt` hash
- 重置密码：默认 `123456`（最省事）或随机密码（更安全）

---

## 7. 本地运行与联调规范

### 7.1 端口
- 后端：`8080`
- 前端：`5173`

### 7.2 联调方式（二选一，全组统一）
**Vite 代理**：前端请求统一走 `/api`

### 7.3 环境配置
- 前端：`.env.development`（如 `VITE_API_BASE=/api`）
- 后端：`application.yml`（PostgreSQL 连接）

---

## 8. 通用组件复用（减少“各做各的”）

建议沉淀到 `frontend/src/components/`：
- `PageContainer`：统一页边距与背景
- `PageHeader`：标题 + 右侧按钮位
- `FormDialog`：统一 dialog 宽度、标题、底部按钮布局
- `ConfirmAction`（可选）：统一二次确认封装

统一空态：
- 表格无数据：`暂无数据`
- 搜索无结果：`未找到匹配记录`



