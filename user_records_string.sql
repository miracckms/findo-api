-- FINDO DB - String ID'li Kullanıcı Kayıtları
-- Bu dosyayı çalıştırmadan önce veritabanına bağlanın: USE findo_db;

-- Foreign key kontrollerini kapat
SET FOREIGN_KEY_CHECKS = 0;

-- Mevcut kayıtları temizle
DELETE FROM ads;
DELETE FROM categories;
DELETE FROM districts;
DELETE FROM cities;
DELETE FROM users;

-- Şehirler
INSERT INTO cities (id, name, plate_code, active, created_at, updated_at) VALUES
('city-ankara', 'Ankara', '06', true, NOW(), NOW()),
('city-istanbul', 'İstanbul', '34', true, NOW(), NOW()),
('city-izmir', 'İzmir', '35', true, NOW(), NOW());

-- İlçeler
INSERT INTO districts (id, name, city_id, active, created_at, updated_at) VALUES
('district-cankaya', 'Çankaya', 'city-ankara', true, NOW(), NOW()),
('district-kecioren', 'Keçiören', 'city-ankara', true, NOW(), NOW()),
('district-besiktas', 'Beşiktaş', 'city-istanbul', true, NOW(), NOW()),
('district-kadikoy', 'Kadıköy', 'city-istanbul', true, NOW(), NOW()),
('district-konak', 'Konak', 'city-izmir', true, NOW(), NOW());

-- Kategoriler
INSERT INTO categories (id, name, description, active, sort_order, created_at, updated_at) VALUES
('cat-elektronik', 'Elektronik', 'Telefon, bilgisayar, tablet vb.', true, 1, NOW(), NOW()),
('cat-ev-dekorasyon', 'Ev & Dekorasyon', 'Mobilya, aksesuar vb.', true, 2, NOW(), NOW()),
('cat-arac', 'Araç', 'Otomobil, motorsiklet vb.', true, 3, NOW(), NOW());

-- Test Kullanıcıları
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, neighborhood, store_mode, city_id, district_id, created_at, updated_at) VALUES
('admin-user-1', 'Admin', 'Findo', 'admin@findo.com', '+905550123456', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'ADMIN', 'ACTIVE', true, true, 'Merkez', false, 'city-ankara', 'district-cankaya', NOW(), NOW()),
('test-user-1', 'Ahmet', 'Yılmaz', 'ahmet@test.com', '+905551234567', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'USER', 'ACTIVE', true, true, 'Ataşehir', false, 'city-istanbul', 'district-kadikoy', NOW(), NOW()),
('test-user-2', 'Fatma', 'Kaya', 'fatma@test.com', '+905559876543', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'USER', 'ACTIVE', true, false, 'Alsancak', true, 'city-izmir', 'district-konak', NOW(), NOW()),
('store-user-1', 'İkinci El', 'Mağaza', 'magaza@findo.com', '+905556789012', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'USER', 'ACTIVE', true, true, 'Çankaya', true, 'city-ankara', 'district-cankaya', NOW(), NOW());

-- Test İlanları
INSERT INTO ads (id, title, description, price, contact_phone, neighborhood, user_id, category_id, city_id, district_id, status, featured, view_count, negotiable, urgent, created_at, updated_at) VALUES
('ad-iphone-13', 'iPhone 13 Pro Max 256GB', 'Temiz iPhone 13 Pro Max, hiç düşürülmemiş, kutulu', 35000.00, '+905551234567', 'Ataşehir', 'test-user-1', 'cat-elektronik', 'city-istanbul', 'district-kadikoy', 'PENDING', false, 5, true, false, NOW(), NOW()),
('ad-macbook-air', 'MacBook Air M2 8GB 256GB', '1 yıllık MacBook Air, garantili', 42000.00, '+905556789012', 'Çankaya', 'store-user-1', 'cat-elektronik', 'city-ankara', 'district-cankaya', 'APPROVED', true, 12, false, false, NOW(), NOW()),
('ad-koltuk-takimi', '3+2+1 Koltuk Takımı', 'Temiz kullanılmış koltuk takımı', 8500.00, '+905559876543', 'Alsancak', 'test-user-2', 'cat-ev-dekorasyon', 'city-izmir', 'district-konak', 'APPROVED', false, 8, true, false, NOW(), NOW());

-- Foreign key kontrollerini aç
SET FOREIGN_KEY_CHECKS = 1;

-- Test sonuçları için basit sorgu
SELECT 'Kullanıcı Sayısı:', COUNT(*) as user_count FROM users;
SELECT 'İlan Sayısı:', COUNT(*) as ad_count FROM ads;
SELECT 'Admin Kullanıcı:', email FROM users WHERE role = 'ADMIN';
