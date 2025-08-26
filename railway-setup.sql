-- Railway Database Setup for FINDO API
-- Run this script in Railway MySQL console

-- Create database (if needed)
-- CREATE DATABASE IF NOT EXISTS railway;
-- USE railway;

-- Drop existing tables if they exist (in correct order to avoid foreign key constraints)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS verification_tokens;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS ad_photos;
DROP TABLE IF EXISTS ads;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS districts;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS categories;

SET FOREIGN_KEY_CHECKS = 1;

-- Create tables
CREATE TABLE categories (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(100),
    parent_id VARCHAR(255),
    sort_order INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE cities (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    plate_code VARCHAR(3) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE districts (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    city_id VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    city_id VARCHAR(255),
    district_id VARCHAR(255),
    neighborhood VARCHAR(100),
    store_mode BOOLEAN DEFAULT FALSE,
    store_name VARCHAR(100),
    store_description TEXT,
    FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE SET NULL,
    FOREIGN KEY (district_id) REFERENCES districts(id) ON DELETE SET NULL
);

CREATE TABLE ads (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    city_id VARCHAR(255) NOT NULL,
    district_id VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(100),
    contact_phone VARCHAR(20) NOT NULL,
    status ENUM('PENDING', 'ACTIVE', 'SOLD', 'REJECTED') DEFAULT 'PENDING',
    rejection_reason TEXT,
    view_count INT DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE,
    FOREIGN KEY (district_id) REFERENCES districts(id) ON DELETE CASCADE
);

CREATE TABLE ad_photos (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ad_id VARCHAR(255) NOT NULL,
    photo_url VARCHAR(500) NOT NULL,
    sort_order INT DEFAULT 0,
    FOREIGN KEY (ad_id) REFERENCES ads(id) ON DELETE CASCADE
);

CREATE TABLE favorites (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id VARCHAR(255) NOT NULL,
    ad_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_ad (user_id, ad_id)
);

CREATE TABLE reports (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    reporter_id VARCHAR(255) NOT NULL,
    ad_id VARCHAR(255) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    status ENUM('PENDING', 'REVIEWED', 'RESOLVED') DEFAULT 'PENDING',
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads(id) ON DELETE CASCADE
);

CREATE TABLE verification_tokens (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id VARCHAR(255) NOT NULL,
    token VARCHAR(6) NOT NULL,
    type ENUM('EMAIL', 'PHONE') NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert initial data
INSERT INTO cities (id, name, plate_code) VALUES
('city-ankara', 'Ankara', '06'),
('city-istanbul', 'İstanbul', '34'),
('city-izmir', 'İzmir', '35');

INSERT INTO districts (id, name, city_id) VALUES
('district-cankaya', 'Çankaya', 'city-ankara'),
('district-kecioren', 'Keçiören', 'city-ankara'),
('district-besiktas', 'Beşiktaş', 'city-istanbul'),
('district-kadikoy', 'Kadıköy', 'city-istanbul'),
('district-bornova', 'Bornova', 'city-izmir'),
('district-konak', 'Konak', 'city-izmir');

INSERT INTO categories (id, name, description, sort_order) VALUES
('cat-elektronik', 'Elektronik', 'Telefon, bilgisayar, tablet vb.', 1),
('cat-ev-dekorasyon', 'Ev & Dekorasyon', 'Mobilya, aksesuar vb.', 2),
('cat-arac', 'Araç', 'Otomobil, motorsiklet vb.', 3);

-- Insert admin user (password: admin123 - BCrypt hash)
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, city_id, district_id, neighborhood) VALUES
('admin-user-1', 'Admin', 'Findo', 'admin@findo.com', '+905550123456', '$2a$10$rQv1Z0p3mKzl7yGa.YxKJOqoOdpL8n.pJH7LXbFuH9fHWF5dYd2PG', 'ADMIN', 'ACTIVE', TRUE, TRUE, 'city-ankara', 'district-cankaya', 'Merkez');

-- Insert sample user
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, city_id, district_id, neighborhood) VALUES
('user-john-1', 'John', 'Doe', 'john@example.com', '+905551234567', '$2a$10$rQv1Z0p3mKzl7yGa.YxKJOqoOdpL8n.pJH7LXbFuH9fHWF5dYd2PG', 'USER', 'ACTIVE', TRUE, TRUE, 'city-istanbul', 'district-kadikoy', 'Fenerbahçe');

-- Insert sample ad
INSERT INTO ads (id, title, description, price, category_id, user_id, city_id, district_id, neighborhood, contact_phone, status) VALUES
('ad-iphone-1', 'iPhone 13 Pro Satılık', 'Temiz kullanılmış iPhone 13 Pro 128GB. Kutulu ve aksesuarlı.', 25000.00, 'cat-elektronik', 'user-john-1', 'city-istanbul', 'district-kadikoy', 'Fenerbahçe', '+905551234567', 'ACTIVE');
