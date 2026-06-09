# Railway 部署指南

## 项目已完成的配置修改

1. ✅ 创建了 `Procfile` 文件 - 用于 Railway 启动应用
2. ✅ 修改了 `application.yml` - 支持环境变量配置
   - 端口使用 `${PORT:8080}` (Railway 会注入 PORT 环境变量)
   - 数据库 URL 使用 `${SPRING_DATASOURCE_URL:...}`
   - 数据库用户名使用 `${SPRING_DATASOURCE_USERNAME:...}`
   - 数据库密码使用 `${SPRING_DATASOURCE_PASSWORD:...}`
3. ✅ 更新了 `.gitignore` 文件

## Railway 部署步骤

### 第一步:推送代码到 GitHub

```bash
# 如果还没有 Git 仓库
git init
git add .
git commit -m "Initial commit for Railway deployment"

# 关联 GitHub 仓库
git remote add origin https://github.com/你的用户名/你的仓库名.git
git push -u origin main
```

### 第二步:在 Railway 创建项目

1. 访问 https://railway.app/ 并登录
2. 点击 "New Project"
3. 选择 "Deploy from GitHub repo"
4. 授权 Railway 访问你的 GitHub
5. 选择你的项目仓库

### 第三步:添加 MySQL 数据库

1. 在 Railway 项目页面,点击 "+ New"
2. 选择 "Database" → "MySQL"
3. 等待数据库服务创建完成

### 第四步:配置环境变量

在你的 Java 应用服务中(不是 MySQL 服务):

1. 点击你的 Java 应用服务
2. 进入 "Variables" 标签页
3. 添加以下变量:

```
SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
SERVER_PORT=${{PORT}}
```

**注意**: Railway 的 MySQL 服务会自动提供这些变量引用,你也可以直接使用 Railway 提供的 `DATABASE_URL`,但需要转换格式。

### 第五步:配置构建和启动

Railway 通常会自动检测 Java/Maven 项目,但你可以手动配置:

1. 进入 "Settings" 标签
2. 确认以下配置:
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/mvc_user_manage-1.0-SNAPSHOT.jar`
   - **Root Directory**: 留空(如果在根目录)

### 第六步:部署

1. Railway 会在检测到代码推送后自动部署
2. 或者手动触发:点击 "Deployments" → "Redeploy"
3. 查看部署日志确保启动成功

### 第七步:生成域名

1. 进入 "Settings" 标签
2. 找到 "Domains" 部分
3. 点击 "Generate Domain"
4. 获得类似 `https://your-project.railway.app` 的访问地址

## 常见问题

### 1. 数据库连接失败
- 检查环境变量是否正确配置
- 确认 Railway MySQL 服务已启动
- 查看日志中的具体错误信息

### 2. 端口问题
- Railway 会注入 `PORT` 环境变量
- 确保 application.yml 中使用 `${PORT:8080}`

### 3. 数据初始化
- `data.sql` 会在首次启动时自动执行
- 检查日志确认 SQL 执行成功

### 4. 内存不足
- Railway 免费套餐有内存限制
- 可以在 Settings 中调整资源分配

## 验证部署

部署成功后,访问 Railway 提供的域名:
- 首页: `https://your-project.railway.app`
- 登录页面: `https://your-project.railway.app/login`
- 注册页面: `https://your-project.railway.app/register`

## 后续更新

每次推送到 GitHub 后,Railway 会自动重新部署:
```bash
git add .
git commit -m "更新说明"
git push
```
