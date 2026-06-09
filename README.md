# 用户管理系统

基于 Spring Boot + Thymeleaf + MySQL 的用户管理系统，支持用户注册、登录、权限管理等功能。

## 🚀 功能特性

### 核心功能
- ✅ **用户注册**：用户名 + 密码注册，无需手机验证码
- ✅ **用户登录**：Session 会话管理，支持记住登录状态
- ✅ **密码加密**：使用 BCrypt 算法加密存储，防止密码泄露
- ✅ **权限控制**：区分管理员和普通用户，不同角色拥有不同权限
- ✅ **用户管理**：管理员可以新增、编辑、删除用户
- ✅ **分页查询**：支持分页展示用户列表
- ✅ **关键字搜索**：支持按用户名或姓名模糊搜索
- ✅ **统一异常处理**：友好的错误提示页面
- ✅ **日志记录**：记录关键操作，方便排查问题

### 权限说明
| 功能 | 普通用户 | 管理员 |
|------|---------|--------|
| 查看用户列表 | ✅ | ✅ |
| 搜索用户 | ✅ | ✅ |
| 编辑个人信息 | ✅ | ✅ |
| 新增用户 | ❌ | ✅ |
| 删除用户 | ❌ | ✅ |

## ️ 技术栈

- **后端框架**：Spring Boot 3.2.5
- **模板引擎**：Thymeleaf
- **持久层**：Spring Data JPA
- **数据库**：MySQL 8.0
- **密码加密**：Spring Security BCrypt
- **参数校验**：Jakarta Validation
- **日志框架**：Logback
- **构建工具**：Maven
- **JDK 版本**：Java 17+

##  快速开始

### 环境要求
- JDK 17 或更高版本
- Maven 3.6+
- MySQL 8.0+

### 安装步骤

#### 1. 克隆项目
```bash
git clone https://github.com/your-username/mvc_user_manage.git
cd mvc_user_manage
```

#### 2. 创建数据库
```sql
CREATE DATABASE user_manage CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 修改配置文件
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_manage?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的数据库密码
```

#### 4. 编译运行
```bash
mvn clean package -DskipTests
java -jar target/mvc_user_manage-1.0-SNAPSHOT.jar
```

#### 5. 访问系统
打开浏览器访问：http://localhost:8082

### 默认账号
系统初始化时会自动创建以下账号（密码均为 `123456`）：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | 管理员 | 拥有全部权限 |
| zhangsan | 123456 | 普通用户 | 仅可查看和编辑个人信息 |
| lisi | 123456 | 普通用户 | 仅可查看和编辑个人信息 |

## 📁 项目结构

```
mvc_user_manage/
├── src/main/java/com/cheng/ming/mvc_user_manage/
│   ├── config/                  # 配置类
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   ├── controller/              # 控制层
│   │   ├── LoginController.java         # 登录/注册控制器
│   │   └── UserController.java          # 用户管理控制器
│   ├── entity/                  # 实体类
│   │   └── User.java                    # 用户实体
│   ├── repository/              # 数据访问层
│   │   └── UserRepository.java          # 用户仓库
│   ├── service/                 # 业务逻辑层
│   │   └── UserService.java             # 用户服务
│   ├── util/                    # 工具类
│   │   └── PasswordUtil.java            # 密码加密工具
│   └── MvcUserManageApplication.java    # 启动类
── src/main/resources/
│   ├── templates/               # 前端页面
│   │   ├── login.html                   # 登录页面
│   │   ├── register.html                # 注册页面
│   │   ├── error.html                   # 错误页面
│   │   ── user/
│   │       ├── list.html                # 用户列表
│   │       ── form.html                # 用户表单（新增/编辑）
│   ├── application.yml          # 应用配置
│   ├── data.sql                 # 初始化数据
│   └── logback.xml              # 日志配置
├── pom.xml                      # Maven 依赖
── README.md                    # 项目文档
```

## 🔐 安全特性

### 密码加密
- 使用 BCrypt 算法对用户密码进行加密存储
- 每次加密生成的密文都不同（自动加盐），防止彩虹表攻击
- 登录时使用 `BCryptPasswordEncoder.matches()` 验证密码

### 权限控制
- 基于 Session 的登录状态管理
- 管理员和普通用户角色分离
- 敏感操作（新增、删除）仅限管理员执行
- 前后端双重权限校验

### 参数校验
- 使用 Jakarta Validation 注解进行表单校验
- 用户名：2-20 个字符，不能为空
- 密码：至少 6 个字符，不能为空
- 邮箱：符合标准邮箱格式
- 姓名：不能为空

## 📝 API 接口

### 认证相关
| 接口 | 方法 | 说明 |
|------|------|------|
| `/login` | GET | 显示登录页面 |
| `/login` | POST | 处理登录请求 |
| `/register` | GET | 显示注册页面 |
| `/register` | POST | 处理注册请求 |
| `/logout` | GET | 退出登录 |

### 用户管理
| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/user/list` | GET | 用户列表（分页） | 所有登录用户 |
| `/user/add` | GET | 显示新增用户页面 | 仅管理员 |
| `/user/add` | POST | 保存新用户 | 仅管理员 |
| `/user/edit/{id}` | GET | 显示编辑用户页面 | 所有登录用户 |
| `/user/edit/{id}` | POST | 更新用户信息 | 所有登录用户 |
| `/user/delete/{id}` | GET | 删除用户 | 仅管理员 |

##  常见问题

### 1. 启动时报错 "Table 'users' doesn't exist"
**原因**：数据库中没有自动建表  
**解决**：检查 `application.yml` 中的 `ddl-auto: update` 配置是否正确

### 2. 登录后访问页面报 403 错误
**原因**：权限不足  
**解决**：确认当前登录用户的角色，普通用户无法访问管理员功能

### 3. 密码加密后无法登录
**原因**：数据库中存储的是明文密码，但代码使用 BCrypt 验证  
**解决**：重新初始化数据库，让 `data.sql` 中的 BCrypt 加密密码生效

### 4. 日志文件在哪里
**位置**：项目根目录下的 `logs/` 文件夹  
**说明**：日志按日期滚动保存，保留最近 30 天

## 📄 许可证

本项目仅供学习参考，未经授权禁止用于商业用途。

## 👨‍💻 作者

- 开发者：Cheng Ming
- 邮箱：your-email@example.com

## 🙏 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Thymeleaf](https://www.thymeleaf.org/)
- [MySQL](https://www.mysql.com/)
