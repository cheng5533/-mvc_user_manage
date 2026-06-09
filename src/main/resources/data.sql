-- 初始化管理员账号（密码都是 123456，已使用 BCrypt 加密）
-- BCrypt 加密后的密码：$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT IGNORE INTO users (username, password, name, email, phone, role, create_time, update_time)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '博主', 'admin@example.com', '13800138000', '管理员', NOW(), NOW());

INSERT IGNORE INTO users (username, password, name, email, phone, role, create_time, update_time)
VALUES ('zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '张三', 'zhangsan@example.com', '13800138001', '普通用户', NOW(), NOW());

-- 初始化分类（只保留生活和工作两类）
INSERT IGNORE INTO categories (name, description, create_time) VALUES ('生活', '日常生活、感悟、旅行等', NOW());
INSERT IGNORE INTO categories (name, description, create_time) VALUES ('工作', '技术学习、项目经验、工作总结等', NOW());

-- 初始化常用标签
INSERT IGNORE INTO tags (name, create_time) VALUES ('Java', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('Spring Boot', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('前端', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('MySQL', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('学习笔记', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('日常', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('随笔', NOW());
INSERT IGNORE INTO tags (name, create_time) VALUES ('旅行', NOW());
