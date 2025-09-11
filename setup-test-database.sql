-- Create test database
CREATE DATABASE IF NOT EXISTS synai_analytics_test;
USE synai_analytics_test;

-- Drop existing table if it exists
DROP TABLE IF EXISTS users;

-- Create users table (matching the original Synai structure)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(36) UNIQUE NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    avatar_url VARCHAR(255),
    locale VARCHAR(10) DEFAULT 'en',
    role VARCHAR(50) DEFAULT 'user',
    mbti VARCHAR(4),
    language VARCHAR(10) DEFAULT 'zh',
    timezone VARCHAR(50) DEFAULT 'UTC',
    preference_tags JSON,
    onboarding_done BOOLEAN DEFAULT FALSE,
    age INT,
    gender VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    provider VARCHAR(50) DEFAULT 'email',
    wechat_openid VARCHAR(100),
    wechat_unionid VARCHAR(100),
    phone VARCHAR(20),
    authing_token VARCHAR(2000),
    authing_user_id VARCHAR(100)
);

-- Insert test users
INSERT INTO users (uuid, email, name, mbti, language, timezone, onboarding_done, age, gender, is_active, created_at) VALUES
('user-001', 'alice@test.com', 'Alice Johnson', 'ENFP', 'en', 'UTC', TRUE, 25, 'female', TRUE, '2024-01-01 10:00:00'),
('user-002', 'bob@test.com', 'Bob Smith', 'INTJ', 'en', 'UTC', TRUE, 30, 'male', TRUE, '2024-01-02 11:00:00'),
('user-003', 'charlie@test.com', 'Charlie Brown', 'ESFP', 'zh', 'UTC+8', FALSE, 22, 'male', TRUE, '2024-01-03 12:00:00'),
('user-004', 'diana@test.com', 'Diana Prince', 'INFJ', 'en', 'UTC', TRUE, 28, 'female', TRUE, '2024-01-04 13:00:00'),
('user-005', 'eve@test.com', 'Eve Wilson', 'ESTJ', 'en', 'UTC', TRUE, 35, 'female', TRUE, '2024-01-05 14:00:00');

-- Show the data
SELECT * FROM users;
