-- FINDO DB - Kullanıcı Kayıtları
-- Bu dosyayı çalıştırmadan önce veritabanına bağlanın: USE findo_db;

-- Yeni Kullanıcı Kayıtları
-- Şifre: "admin123", "user123", "test123" şifreleri için BCrypt hash
-- Hash: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2 (admin123)
-- Hash: $2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO (password123)

-- 1. Admin Kullanıcı (Terminal loglarında aranan kullanıcı)
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, neighborhood, store_mode, store_name, store_description, city_id, district_id, created_at, updated_at) VALUES
(1, 'Admin', 'Findo', 'admin@findo.com', '+905550123456', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'ADMIN', 'ACTIVE', true, true, 'Merkez', false, NULL, NULL, 1, 1, NOW(), NOW());

-- 2. Test Kullanıcıları
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, neighborhood, store_mode, store_name, store_description, city_id, district_id, created_at, updated_at) VALUES
-- Normal kullanıcı
(2, 'Test', 'User', 'test@findo.com', '+905550123457', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'ACTIVE', true, true, 'Test Mahallesi', false, NULL, NULL, 1, 1, NOW(), NOW()),

-- Mağaza kullanıcısı
(3, 'Mağaza', 'Sahibi', 'magaza@findo.com', '+905550123458', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'ACTIVE', true, true, 'İş Merkezi', true, 'Test Mağazası', 'Güvenilir ve kaliteli ürünler sunan test mağazası', 1, 1, NOW(), NOW()),

-- Moderatör
(4, 'Moderator', 'User', 'moderator@findo.com', '+905550123459', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'MODERATOR', 'ACTIVE', true, true, 'Merkez', false, NULL, NULL, 1, 1, NOW(), NOW()),

-- Bekleyen doğrulama kullanıcısı
(5, 'Pending', 'User', 'pending@findo.com', '+905550123460', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'PENDING_VERIFICATION', false, false, 'Test Bölgesi', false, NULL, NULL, 1, 1, NOW(), NOW());

-- Şehir ve İlçe verilerini ekleyelim (eğer mevcut değilse)
INSERT IGNORE INTO cities (id, name, plate_code, active, created_at, updated_at) VALUES
(1, 'İstanbul', '34', true, NOW(), NOW()),
(2, 'Ankara', '06', true, NOW(), NOW()),
(3, 'İzmir', '35', true, NOW(), NOW());

INSERT IGNORE INTO districts (id, name, active, city_id, created_at, updated_at) VALUES
(1, 'Kadıköy', true, 1, NOW(), NOW()),
(2, 'Beşiktaş', true, 1, NOW(), NOW()),
(3, 'Çankaya', true, 2, NOW(), NOW()),
(4, 'Konak', true, 3, NOW(), NOW());

-- Kategoriler (eğer mevcut değilse)
INSERT IGNORE INTO categories (id, name, description, icon, sort_order, active, parent_id, created_at, updated_at) VALUES
(1, 'Elektronik', 'Elektronik ürünler ve aksesuarlar', 'electronics', 1, true, NULL, NOW(), NOW()),
(2, 'Emlak', 'Satılık ve kiralık gayrimenkuller', 'real-estate', 2, true, NULL, NOW(), NOW()),
(3, 'Araçlar', 'Otomobil, motosiklet ve diğer araçlar', 'vehicles', 3, true, NULL, NOW(), NOW());

-- Test İlanları (Kullanıcılar için)
INSERT INTO ads (id, title, description, price, negotiable, status, view_count, published_at, expires_at, featured, urgent, neighborhood, contact_phone, user_id, category_id, city_id, district_id, created_at, updated_at) VALUES
-- Admin'in ilanı
(1, 'Test iPhone 13', 'Test amaçlı iPhone 13 ilanı. Temiz ve bakımlı.', 30000.00, true, 'ACTIVE', 10, NOW(), NOW() + INTERVAL 30 DAY, false, false, 'Merkez', '+905550123456', 1, 1, 1, 1, NOW(), NOW()),

-- Test kullanıcısının ilanı
(2, 'Samsung Galaxy S22', 'Test kullanıcısının Samsung telefonu ilanı.', 25000.00, false, 'ACTIVE', 5, NOW(), NOW() + INTERVAL 30 DAY, false, false, 'Test Mahallesi', '+905550123457', 2, 1, 1, 1, NOW(), NOW()),

-- Mağaza sahibinin ilanı
(3, 'MacBook Pro M2', 'Mağazamızdan garantili MacBook Pro satışı.', 45000.00, true, 'ACTIVE', 15, NOW(), NOW() + INTERVAL 30 DAY, true, false, 'İş Merkezi', '+905550123458', 3, 1, 1, 1, NOW(), NOW());

-- Giriş Bilgileri:
-- ==================
-- Admin: admin@findo.com / admin123
-- Test Kullanıcı: test@findo.com / password123
-- Mağaza Sahibi: magaza@findo.com / password123
-- Moderatör: moderator@findo.com / password123
-- Bekleyen Kullanıcı: pending@findo.com / password123

COMMIT;
