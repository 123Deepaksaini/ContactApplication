-- Contact Application Database Schema for Railway MySQL

CREATE DATABASE IF NOT EXISTS railway;
USE railway;

-- User Table
CREATE TABLE IF NOT EXISTS user (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    loginName VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role INT DEFAULT 1, -- 1 = USER, 2 = ADMIN
    loginStatus INT DEFAULT 1 -- 1 = ACTIVE, 0 = BLOCKED
);

-- Contact Table
CREATE TABLE IF NOT EXISTS contact (
    contactId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    remark VARCHAR(255)
);

-- Insert default admin user
INSERT INTO user (name, phone, email, address, loginName, password, role, loginStatus) 
VALUES ('Admin', '1234567890', 'admin@capp.in', 'CAPP Office', 'admin', 'admin123', 2, 1);

-- Insert sample user
INSERT INTO user (name, phone, email, address, loginName, password, role, loginStatus) 
VALUES ('Deepak', '9876543210', 'deepak@capp.in', 'India', 'deepak', 'deepak123', 1, 1);
