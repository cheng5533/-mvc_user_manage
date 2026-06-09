-- 修复评论表的 create_time 字段 NULL 值问题
-- 执行此脚本可以解决发表评论时的 "Column 'create_time' cannot be null" 错误

-- 1. 查看有多少条评论的 create_time 是 NULL
SELECT COUNT(*) AS null_count FROM comments WHERE create_time IS NULL;

-- 2. 将所有 create_time 为 NULL 的记录更新为当前时间
UPDATE comments SET create_time = NOW() WHERE create_time IS NULL;

-- 3. 验证修改结果(应该返回 0)
SELECT COUNT(*) AS remaining_null FROM comments WHERE create_time IS NULL;

-- 4. 检查表结构,确保 create_time 字段有默认值
DESCRIBE comments;
