-- Contact Application Database Schema (idempotent)
-- Safe to run multiple times.

SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- Run this script on your `contact_ap` database.
USE `contact_ap`;

CREATE TABLE IF NOT EXISTS `user` (
    userId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    loginName VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role INT NOT NULL DEFAULT 1,        -- 1 = USER, 2 = ADMIN
    loginStatus INT NOT NULL DEFAULT 1  -- 1 = ACTIVE, 0 = BLOCKED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS contact (
    contactId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    remark VARCHAR(255),
    INDEX idx_contact_userId (userId),
    CONSTRAINT fk_contact_user
        FOREIGN KEY (userId) REFERENCES `user`(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed users: rerun-safe (updates existing rows if loginName already exists)
INSERT INTO `user` (name, phone, email, address, loginName, password, role, loginStatus)
VALUES
    ('Admin', '1234567890', 'admin@capp.in', 'CAPP Office', 'admin', 'admin123', 2, 1),
    ('Deepak', '9876543210', 'deepak@capp.in', 'India', 'deepak', 'deepak123', 1, 1)
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    phone = VALUES(phone),
    email = VALUES(email),
    address = VALUES(address),
    password = VALUES(password),
    role = VALUES(role),
    loginStatus = VALUES(loginStatus);
