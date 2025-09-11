-- Create users table for Synai analytics
CREATE TABLE IF NOT EXISTS users (
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
    onboarding_done BOOLEAN DEFAULT FALSE,
    age INT,
    gender VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT NOW(),
    updated_at DATETIME DEFAULT NOW(),
    provider VARCHAR(50) DEFAULT 'email'
);

-- Insert some test data
INSERT INTO users (uuid, email, name, mbti, age, gender, created_at) VALUES
('user-001', 'test1@example.com', 'Test User 1', 'ENFP', 25, 'female', '2024-01-01 10:00:00'),
('user-002', 'test2@example.com', 'Test User 2', 'INTJ', 30, 'male', '2024-01-02 11:00:00'),
('user-003', 'test3@example.com', 'Test User 3', 'ESFP', 22, 'female', '2024-01-03 12:00:00'),
('user-004', 'test4@example.com', 'Test User 4', 'INTP', 28, 'male', '2024-01-04 13:00:00'),
('user-005', 'test5@example.com', 'Test User 5', 'ENFJ', 35, 'female', '2024-01-05 14:00:00');
