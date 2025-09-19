# SynAI 阿里云 ECS 部署文档

本文档详细描述了如何将 SynAI 情感交流平台部署到阿里云 ECS 服务器的完整流程。

## 部署概览

### 服务器架构
- **服务器 A (120.26.82.225)**: 运行所有微服务 + MySQL 数据库
- **服务器 B (47.110.77.32)**: 运行 MongoDB 数据库

### 部署的服务
- **user-service** (端口 8000): 用户管理、认证服务
- **message-service** (端口 8003): 消息处理服务
- **model-service** (端口 8005): AI 模型推理服务

---

## 前置条件

### 本地环境要求
- SSH 密钥文件: `~/SynAI/synai-test-env-key.pem`
- rsync 工具 (用于代码同步)
- 本地项目代码已更新并测试通过

### 远程服务器要求
- **服务器 A**: Docker, Python 3.12, 已运行的 MySQL 容器
- **服务器 B**: Docker, 已运行的 MongoDB 容器
- 两台服务器间网络连通性良好

---

## 部署步骤

### Step 1: 备份现有服务

连接到服务器并备份当前代码：

```bash
# 连接到服务器
ssh -i ~/SynAI/synai-test-env-key.pem root@120.26.82.225

# 创建备份
cd /opt/synai
cp -r backend backend_backup_$(date +%Y%m%d_%H%M%S)
```

### Step 2: 数据库更新

#### 2.1 上传数据库更新脚本

```bash
# 从本地上传 SQL 脚本
scp -i ~/SynAI/synai-test-env-key.pem \
    create_users_table.sql \
    create_invitation_code_usage_table.sql \
    add_invitation_code_column.sql \
    root@120.26.82.225:/opt/synai/
```

#### 2.2 应用数据库更改

```bash
# 在服务器上执行
ssh -i ~/SynAI/synai-test-env-key.pem root@120.26.82.225

cd /opt/synai

# 添加 has_valid_invitation_code 字段
docker exec mysql-synai mysql -u root -proot synai_user < add_invitation_code_column.sql

# 创建 invitation_code_usage 表
docker exec mysql-synai mysql -u root -proot synai_user < create_invitation_code_usage_table.sql
```

#### 2.3 验证数据库更改

```bash
# 验证字段添加
docker exec mysql-synai mysql -u root -proot synai_user -e "DESCRIBE users;" | grep has_valid_invitation_code

# 验证表创建
docker exec mysql-synai mysql -u root -proot synai_user -e "SHOW TABLES;" | grep invitation_code_usage
```

### Step 3: 部署 User Service

#### 3.1 同步代码

```bash
# 从本地同步 user-service 代码
rsync -avz --delete \
    -e "ssh -i ~/SynAI/synai-test-env-key.pem" \
    backend/user-service/ \
    root@120.26.82.225:/opt/synai/backend/user-service/ \
    --exclude=venv --exclude=__pycache__ --exclude=.env
```

#### 3.2 重启服务

```bash
# 连接到服务器
ssh -i ~/SynAI/synai-test-env-key.pem root@120.26.82.225

# 停止现有服务
pkill -f 'uvicorn app.main:app.*port 8000'

# 启动新服务
cd /opt/synai/backend/user-service
nohup /opt/synai/backend/user-service/venv/bin/uvicorn \
    app.main:app --host 127.0.0.1 --port 8000 --proxy-headers \
    > /var/log/user-service.log 2>&1 &
```

#### 3.3 验证服务

```bash
# 检查服务状态
ps aux | grep uvicorn | grep 8000

# 健康检查
curl -s http://127.0.0.1:8000/health
# 预期输出: {"status":"healthy"}
```

### Step 4: 部署 Message Service

#### 4.1 同步代码

```bash
# 从本地同步 message-service 代码
rsync -avz --delete \
    -e "ssh -i ~/SynAI/synai-test-env-key.pem" \
    backend/message-service/ \
    root@120.26.82.225:/opt/synai/backend/message-service/ \
    --exclude=venv --exclude=__pycache__ --exclude=.env
```

#### 4.2 重启服务

```bash
# 停止现有服务
pkill -f 'uvicorn app.main:app.*port 8003'

# 启动新服务
cd /opt/synai/backend/message-service
nohup /opt/synai/backend/message-service/venv/bin/uvicorn \
    app.main:app --host 127.0.0.1 --port 8003 --proxy-headers \
    > /var/log/message-service.log 2>&1 &
```

#### 4.3 验证服务

```bash
# 检查服务状态
ps aux | grep uvicorn | grep 8003

# 健康检查
curl -s http://127.0.0.1:8003/health
# 预期输出: {"status":"healthy","database":"MongoDB","database_status":"connected"}
```

### Step 5: 部署 Model Service

#### 5.1 同步代码

```bash
# 从本地同步 model-service 代码
rsync -avz --delete \
    -e "ssh -i ~/SynAI/synai-test-env-key.pem" \
    backend/model-service/ \
    root@120.26.82.225:/opt/synai/backend/model-service/ \
    --exclude=venv --exclude=__pycache__ --exclude=.env --exclude=logs
```

#### 5.2 设置环境

```bash
# 连接到服务器
ssh -i ~/SynAI/synai-test-env-key.pem root@120.26.82.225

cd /opt/synai/backend/model-service

# 检查并创建虚拟环境（如果不存在）
if [ ! -d "venv" ]; then
    python3 -m venv venv
    source venv/bin/activate
    pip install -r /opt/synai/backend/requirements.txt
fi
```

#### 5.3 启动服务

```bash
# 启动 model-service
nohup /opt/synai/backend/model-service/venv/bin/uvicorn \
    app.main:app --host 127.0.0.1 --port 8005 --proxy-headers \
    > /var/log/model-service.log 2>&1 &
```

#### 5.4 验证服务

```bash
# 检查服务状态
ps aux | grep uvicorn | grep 8005

# 检查 API 文档可访问性
curl -s http://127.0.0.1:8005/docs | head -3
```

---

## 部署验证

### 服务状态检查

```bash
# 检查所有运行的服务
ps aux | grep uvicorn | grep -v grep

# 预期输出应包含三个服务进程：
# - port 8000 (user-service)
# - port 8003 (message-service) 
# - port 8005 (model-service)
```

### 健康检查

```bash
# User Service
curl -s http://127.0.0.1:8000/health
# 预期: {"status":"healthy"}

# Message Service  
curl -s http://127.0.0.1:8003/health
# 预期: {"status":"healthy","database":"MongoDB","database_status":"connected"}

# Model Service
curl -s http://127.0.0.1:8005/docs > /dev/null && echo "OK" || echo "FAIL"
# 预期: OK
```

### 数据库连接验证

```bash
# MySQL 连接检查
docker exec mysql-synai mysql -u root -proot synai_user -e "SELECT COUNT(*) as user_count FROM users; SELECT COUNT(*) as invitation_usage_count FROM invitation_code_usage;"

# MongoDB 连接检查
curl -s http://127.0.0.1:8003/health | grep -o '"database_status":"[^"]*"'
# 预期: "database_status":"connected"
```

---

## 故障排查

### 常见问题

#### 1. 服务启动失败

```bash
# 查看服务日志
tail -f /var/log/user-service.log
tail -f /var/log/message-service.log  
tail -f /var/log/model-service.log
```

#### 2. 数据库连接问题

```bash
# 检查 MySQL 容器状态
docker ps | grep mysql-synai

# 检查 MongoDB 连接
telnet 47.110.77.32 27017
```

#### 3. 端口占用

```bash
# 查看端口占用
netstat -tlnp | grep -E ':(8000|8003|8005)'

# 强制终止进程
pkill -f 'uvicorn.*port 8000'
```

### 回滚流程

如果部署出现问题，可以快速回滚：

```bash
# 停止所有服务
pkill -f uvicorn

# 恢复备份
cd /opt/synai
rm -rf backend
mv backend_backup_YYYYMMDD_HHMMSS backend

# 重启服务
cd backend/user-service && nohup venv/bin/uvicorn app.main:app --host 127.0.0.1 --port 8000 --proxy-headers > /var/log/user-service.log 2>&1 &
cd backend/message-service && nohup venv/bin/uvicorn app.main:app --host 127.0.0.1 --port 8003 --proxy-headers > /var/log/message-service.log 2>&1 &
```

