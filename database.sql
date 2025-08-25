-- Findo API Database Setup Script
-- MySQL 8.0+ required

-- Create database
CREATE DATABASE IF NOT EXISTS findo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user (optional, you can use root)
CREATE USER IF NOT EXISTS 'findo_user'@'localhost' IDENTIFIED BY 'findo_password';
GRANT ALL PRIVILEGES ON findo_db.* TO 'findo_user'@'localhost';
FLUSH PRIVILEGES;

-- Use the database
USE findo_db;

-- Note: Tables will be automatically created by JPA/Hibernate
-- This script is provided for reference and manual setup if needed

-- Sample data insert script (optional - DataLoader handles this)
-- You can run this after the application starts if you want additional data

-- Additional sample categories
INSERT IGNORE INTO categories (id, name, description, icon, sort_order, active, parent_id, created_at, updated_at) VALUES
(100, 'İş Makineleri', 'İnşaat ve sanayi makineleri', 'bulldozer', 10, true, NULL, NOW(), NOW()),
(101, 'Spor & Outdoor', 'Spor malzemeleri ve outdoor ürünler', 'basketball', 11, true, NULL, NOW(), NOW()),
(102, 'Kitap & Müzik', 'Kitaplar, müzik enstrümanları', 'book', 12, true, NULL, NOW(), NOW()),
(103, 'Bebek & Çocuk', 'Bebek ve çocuk ürünleri', 'baby-face', 13, true, NULL, NOW(), NOW()),
(104, 'Hayvanlar', 'Evcil hayvanlar ve ürünleri', 'paw', 14, true, NULL, NOW(), NOW());

-- Additional sample cities and districts
INSERT IGNORE INTO cities (id, name, plate_code, active, created_at, updated_at) VALUES
(100, 'Adana', '01', true, NOW(), NOW()),
(101, 'Adıyaman', '02', true, NOW(), NOW()),
(102, 'Afyon', '03', true, NOW(), NOW()),
(103, 'Ağrı', '04', true, NOW(), NOW()),
(104, 'Amasya', '05', true, NOW(), NOW()),
(105, 'Balıkesir', '10', true, NOW(), NOW()),
(106, 'Bilecik', '11', true, NOW(), NOW()),
(107, 'Bingöl', '12', true, NOW(), NOW()),
(108, 'Bitlis', '13', true, NOW(), NOW()),
(109, 'Bolu', '14', true, NOW(), NOW()),
(110, 'Burdur', '15', true, NOW(), NOW()),
(111, 'Çanakkale', '17', true, NOW(), NOW()),
(112, 'Çankırı', '18', true, NOW(), NOW()),
(113, 'Çorum', '19', true, NOW(), NOW()),
(114, 'Denizli', '20', true, NOW(), NOW()),
(115, 'Diyarbakır', '21', true, NOW(), NOW()),
(116, 'Edirne', '22', true, NOW(), NOW()),
(117, 'Elazığ', '23', true, NOW(), NOW()),
(118, 'Erzincan', '24', true, NOW(), NOW()),
(119, 'Erzurum', '25', true, NOW(), NOW()),
(120, 'Eskişehir', '26', true, NOW(), NOW()),
(121, 'Gaziantep', '27', true, NOW(), NOW()),
(122, 'Giresun', '28', true, NOW(), NOW()),
(123, 'Gümüşhane', '29', true, NOW(), NOW()),
(124, 'Hakkari', '30', true, NOW(), NOW()),
(125, 'Hatay', '31', true, NOW(), NOW()),
(126, 'Isparta', '32', true, NOW(), NOW()),
(127, 'Mersin', '33', true, NOW(), NOW()),
(128, 'Kayseri', '38', true, NOW(), NOW()),
(129, 'Kırklareli', '39', true, NOW(), NOW()),
(130, 'Kırşehir', '40', true, NOW(), NOW()),
(131, 'Kocaeli', '41', true, NOW(), NOW()),
(132, 'Konya', '42', true, NOW(), NOW()),
(133, 'Kütahya', '43', true, NOW(), NOW()),
(134, 'Malatya', '44', true, NOW(), NOW()),
(135, 'Manisa', '45', true, NOW(), NOW()),
(136, 'Muğla', '48', true, NOW(), NOW()),
(137, 'Samsun', '55', true, NOW(), NOW()),
(138, 'Trabzon', '61', true, NOW(), NOW());

-- Sample districts for some cities
INSERT IGNORE INTO districts (id, name, active, city_id, created_at, updated_at) VALUES
-- Adana districts
(500, 'Seyhan', true, 100, NOW(), NOW()),
(501, 'Yüreğir', true, 100, NOW(), NOW()),
(502, 'Çukurova', true, 100, NOW(), NOW()),
(503, 'Sarıçam', true, 100, NOW(), NOW()),

-- Bursa districts (assuming Bursa has ID from initial data)
(504, 'Osmangazi', true, (SELECT id FROM cities WHERE name = 'Bursa'), NOW(), NOW()),
(505, 'Nilüfer', true, (SELECT id FROM cities WHERE name = 'Bursa'), NOW(), NOW()),
(506, 'Yıldırım', true, (SELECT id FROM cities WHERE name = 'Bursa'), NOW(), NOW()),

-- Antalya districts
(507, 'Muratpaşa', true, (SELECT id FROM cities WHERE name = 'Antalya'), NOW(), NOW()),
(508, 'Kepez', true, (SELECT id FROM cities WHERE name = 'Antalya'), NOW(), NOW()),
(509, 'Konyaaltı', true, (SELECT id FROM cities WHERE name = 'Antalya'), NOW(), NOW()),
(510, 'Aksu', true, (SELECT id FROM cities WHERE name = 'Antalya'), NOW(), NOW());

-- Create indexes for better performance (optional)
-- These will help with search queries

-- Indexes for ads table
CREATE INDEX IF NOT EXISTS idx_ads_status ON ads(status);
CREATE INDEX IF NOT EXISTS idx_ads_category ON ads(category_id);
CREATE INDEX IF NOT EXISTS idx_ads_city ON ads(city_id);
CREATE INDEX IF NOT EXISTS idx_ads_district ON ads(district_id);
CREATE INDEX IF NOT EXISTS idx_ads_price ON ads(price);
CREATE INDEX IF NOT EXISTS idx_ads_created_at ON ads(created_at);
CREATE INDEX IF NOT EXISTS idx_ads_published_at ON ads(published_at);

-- Indexes for users table
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);

-- Indexes for categories table
CREATE INDEX IF NOT EXISTS idx_categories_parent ON categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_categories_active ON categories(active);

-- Indexes for verification tokens
CREATE INDEX IF NOT EXISTS idx_verification_tokens_token ON verification_tokens(token);
CREATE INDEX IF NOT EXISTS idx_verification_tokens_user ON verification_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_verification_tokens_expires ON verification_tokens(expires_at);

-- Full-text search index for ad title and description (optional)
-- ALTER TABLE ads ADD FULLTEXT(title, description);

COMMIT;
