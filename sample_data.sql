-- FINDO DB Sample Data
-- Önce veritabanını oluşturun: CREATE DATABASE findo_db;
-- Bu dosyayı çalıştırmadan önce: USE findo_db;

-- Şehirler (Cities)
INSERT INTO cities (id, name, plate_code, active, created_at, updated_at) VALUES
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), 'İstanbul', '34', true, NOW(), NOW()),
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), 'Ankara', '06', true, NOW(), NOW()),
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), 'İzmir', '35', true, NOW(), NOW()),
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440004', '-', '')), 'Bursa', '16', true, NOW(), NOW()),
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440005', '-', '')), 'Adana', '01', true, NOW(), NOW()),
(UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440006', '-', '')), 'Antalya', '07', true, NOW(), NOW());

-- İlçeler (Districts)
INSERT INTO districts (id, name, active, city_id, created_at, updated_at) VALUES
-- İstanbul İlçeleri
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), 'Kadıköy', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')), 'Beşiktaş', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440003', '-', '')), 'Şişli', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440004', '-', '')), 'Üsküdar', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
-- Ankara İlçeleri
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440005', '-', '')), 'Çankaya', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440006', '-', '')), 'Keçiören', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
-- İzmir İlçeleri
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440007', '-', '')), 'Konak', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440008', '-', '')), 'Bornova', true, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW());

-- Ana Kategoriler (Parent Categories)
INSERT INTO categories (id, name, description, icon, sort_order, active, parent_id, created_at, updated_at) VALUES
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')), 'Elektronik', 'Elektronik ürünler ve aksesuarlar', 'electronics', 1, true, NULL, NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440002', '-', '')), 'Emlak', 'Satılık ve kiralık gayrimenkuller', 'real-estate', 2, true, NULL, NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440003', '-', '')), 'Araçlar', 'Otomobil, motosiklet ve diğer araçlar', 'vehicles', 3, true, NULL, NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440004', '-', '')), 'Ev & Bahçe', 'Ev eşyaları ve bahçe ürünleri', 'home-garden', 4, true, NULL, NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440005', '-', '')), 'Moda & Giyim', 'Giyim, ayakkabı ve aksesuar', 'fashion', 5, true, NULL, NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440006', '-', '')), 'Hobi & Spor', 'Spor malzemeleri ve hobi ürünleri', 'sports-hobby', 6, true, NULL, NOW(), NOW());

-- Alt Kategoriler (Child Categories)
INSERT INTO categories (id, name, description, icon, sort_order, active, parent_id, created_at, updated_at) VALUES
-- Elektronik Alt Kategorileri
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440011', '-', '')), 'Cep Telefonu', 'Akıllı telefon ve cep telefonu', 'phone', 1, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440012', '-', '')), 'Bilgisayar', 'Masaüstü ve taşınabilir bilgisayarlar', 'computer', 2, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440013', '-', '')), 'TV & Ses Sistemleri', 'Televizyon ve ses sistemleri', 'tv', 3, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
-- Emlak Alt Kategorileri
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440021', '-', '')), 'Satılık Daire', 'Satılık konut ve daireler', 'apartment', 1, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440022', '-', '')), 'Kiralık Daire', 'Kiralık konut ve daireler', 'rent', 2, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
-- Araçlar Alt Kategorileri
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440031', '-', '')), 'Otomobil', 'Binek otomobilleri', 'car', 1, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440032', '-', '')), 'Motosiklet', 'Motosiklet ve scooter', 'motorcycle', 2, true, UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW());

-- Kullanıcılar (Users)
INSERT INTO users (id, first_name, last_name, email, phone, password, role, status, email_verified, phone_verified, neighborhood, store_mode, store_name, store_description, city_id, district_id, created_at, updated_at) VALUES
-- Admin Kullanıcı
(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440001', '-', '')), 'Admin', 'User', 'admin@findo.com', '+905551234567', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'ADMIN', 'ACTIVE', true, true, 'Merkez', false, NULL, NULL, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),

-- Normal Kullanıcılar
(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), 'Ahmet', 'Yılmaz', 'ahmet@example.com', '+905551234568', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'ACTIVE', true, true, 'Fenerbahçe', false, NULL, NULL, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),

(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440003', '-', '')), 'Ayşe', 'Demir', 'ayse@example.com', '+905551234569', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'ACTIVE', true, true, 'Etiler', true, 'Ayşe\'nin Mağazası', 'Kaliteli ürünler ve güvenilir hizmet', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),

(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440004', '-', '')), 'Mehmet', 'Kaya', 'mehmet@example.com', '+905551234570', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'USER', 'ACTIVE', true, false, 'Kızılay', false, NULL, NULL, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440005', '-', '')), NOW(), NOW()),

(UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440005', '-', '')), 'Fatma', 'Öz', 'fatma@example.com', '+905551234571', '$2a$10$N3A7LpGNTlOBwGzTrEjEzOQDJ8KQiGVhFAOYO5YnK7rGqgBpKt4SO', 'MODERATOR', 'ACTIVE', true, true, 'Alsancak', false, NULL, NULL, UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440007', '-', '')), NOW(), NOW());

-- İlanlar (Ads)
INSERT INTO ads (id, title, description, price, negotiable, status, view_count, published_at, expires_at, featured, urgent, neighborhood, contact_phone, user_id, category_id, city_id, district_id, created_at, updated_at) VALUES

-- Elektronik İlanları
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), 'iPhone 14 Pro Max 256GB Temiz', 'Az kullanılmış iPhone 14 Pro Max. Kutulu, şarj aleti mevcut. Hiçbir sorunu yok. Sadece nakit satış.', 45000.00, true, 'ACTIVE', 125, NOW() - INTERVAL 2 DAY, NOW() + INTERVAL 28 DAY, true, false, 'Fenerbahçe', '+905551234568', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440011', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), NOW() - INTERVAL 2 DAY, NOW()),

(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002', '-', '')), 'Gaming Laptop MSI Katana 15', 'MSI Katana 15 gaming laptop. RTX 3060, Intel i7, 16GB RAM, 1TB SSD. Oyun için mükemmel performans.', 25000.00, false, 'ACTIVE', 89, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 29 DAY, false, true, 'Etiler', '+905551234569', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440003', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440012', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440002', '-', '')), NOW() - INTERVAL 1 DAY, NOW()),

-- Emlak İlanları
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440003', '-', '')), '3+1 Satılık Daire Kadıköy', 'Kadıköy merkezde 3+1 satılık daire. 120 m², balkonlu, asansörlü bina. Deniz manzaralı.', 2500000.00, true, 'ACTIVE', 234, NOW() - INTERVAL 3 DAY, NOW() + INTERVAL 27 DAY, true, false, 'Fenerbahçe', '+905551234568', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440021', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), NOW() - INTERVAL 3 DAY, NOW()),

(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440004', '-', '')), '2+1 Kiralık Daire Çankaya', 'Çankaya merkezde kiralık 2+1 daire. Eşyalı, otopark mevcut. Metro yakını.', 8500.00, false, 'ACTIVE', 156, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 29 DAY, false, false, 'Kızılay', '+905551234570', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440004', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440022', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440005', '-', '')), NOW() - INTERVAL 1 DAY, NOW()),

-- Araç İlanları
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440005', '-', '')), '2019 Model Volkswagen Golf', '2019 model Volkswagen Golf 1.6 TDI. 85.000 km de. Hasar kayıtsız, tek elden. Full bakımlı.', 650000.00, true, 'ACTIVE', 98, NOW() - INTERVAL 2 DAY, NOW() + INTERVAL 28 DAY, false, false, 'Alsancak', '+905551234571', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440005', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440031', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440007', '-', '')), NOW() - INTERVAL 2 DAY, NOW()),

-- Beklemede olan ilan
(UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440006', '-', '')), 'Samsung 65" 4K Smart TV', 'Samsung 65 inç 4K Smart TV. 2022 model, garantili. Az kullanılmış, tertemiz.', 18000.00, false, 'PENDING_APPROVAL', 0, NULL, NULL, false, false, 'Fenerbahçe', '+905551234568', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('770e8400-e29b-41d4-a716-446655440013', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('660e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW());

-- Favoriler (Favorites)
INSERT INTO favorites (id, user_id, ad_id, created_at, updated_at) VALUES
(UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440001', '-', '')), UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440003', '-', '')), UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('aa0e8400-e29b-41d4-a716-446655440003', '-', '')), UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440004', '-', '')), UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW());

-- İlan Fotoğrafları (Ad Photos)
INSERT INTO ad_photos (id, file_name, file_path, file_size, mime_type, sort_order, ad_id, created_at, updated_at) VALUES
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440001', '-', '')), 'iphone14pro_1.jpg', '/uploads/ads/iphone14pro_1.jpg', 245678, 'image/jpeg', 1, UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440002', '-', '')), 'iphone14pro_2.jpg', '/uploads/ads/iphone14pro_2.jpg', 234567, 'image/jpeg', 2, UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440003', '-', '')), 'gaming_laptop_1.jpg', '/uploads/ads/gaming_laptop_1.jpg', 356789, 'image/jpeg', 1, UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440004', '-', '')), 'apartment_1.jpg', '/uploads/ads/apartment_1.jpg', 445678, 'image/jpeg', 1, UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('bb0e8400-e29b-41d4-a716-446655440005', '-', '')), 'apartment_2.jpg', '/uploads/ads/apartment_2.jpg', 456789, 'image/jpeg', 2, UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW());

-- Raporlar (Reports)
INSERT INTO reports (id, reason, description, status, admin_notes, reporter_id, ad_id, reviewed_by_id, created_at, updated_at) VALUES
(UNHEX(REPLACE('cc0e8400-e29b-41d4-a716-446655440001', '-', '')), 'Yanıltıcı ilan', 'İlanda belirtilen fiyat ile gerçek fiyat farklı', 'PENDING', NULL, UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440004', '-', '')), UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440001', '-', '')), NULL, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
(UNHEX(REPLACE('cc0e8400-e29b-41d4-a716-446655440002', '-', '')), 'Sahte ilan', 'Bu ürün gerçekte satılık değil', 'REVIEWED', 'İncelendi, sorun bulunamadı', UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('990e8400-e29b-41d4-a716-446655440002', '-', '')), UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440005', '-', '')), NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY);

-- Doğrulama Tokenları (Verification Tokens) - Örnek olarak
INSERT INTO verification_tokens (id, token, token_type, expires_at, used, user_id, created_at, updated_at) VALUES
(UNHEX(REPLACE('dd0e8400-e29b-41d4-a716-446655440001', '-', '')), '123456', 'EMAIL_VERIFICATION', NOW() + INTERVAL 1 HOUR, false, UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440002', '-', '')), NOW(), NOW()),
(UNHEX(REPLACE('dd0e8400-e29b-41d4-a716-446655440002', '-', '')), '789012', 'PASSWORD_RESET', NOW() + INTERVAL 1 HOUR, false, UNHEX(REPLACE('880e8400-e29b-41d4-a716-446655440003', '-', '')), NOW(), NOW());

-- NOT: Şifreler bcrypt ile hash'lenmiş durumda (password123)
-- Test için kullanabileceğiniz giriş bilgileri:
-- Admin: admin@findo.com / password123
-- Kullanıcı: ahmet@example.com / password123
-- Mağaza Sahibi: ayse@example.com / password123
