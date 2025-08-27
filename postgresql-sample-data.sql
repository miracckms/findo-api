-- FINDO DB Sample Data for PostgreSQL
-- Bu dosya Spring Boot uygulaması çalıştırıldıktan sonra manuel olarak çalıştırılabilir
-- Tablolar otomatik oluşacak (hibernate ddl-auto=update sayesinde)

-- Şehirler (Cities)
INSERT INTO cities (id, name, plate_code, active, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'İstanbul', '34', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440002', 'Ankara', '06', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440003', 'İzmir', '35', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440004', 'Bursa', '16', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440005', 'Adana', '01', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440006', 'Antalya', '07', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- İlçeler (Districts)
INSERT INTO districts (id, name, active, city_id, created_at, updated_at) VALUES
-- İstanbul İlçeleri
('660e8400-e29b-41d4-a716-446655440001', 'Kadıköy', true, '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440002', 'Beşiktaş', true, '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440003', 'Şişli', true, '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440004', 'Üsküdar', true, '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440005', 'Beyoğlu', true, '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),

-- Ankara İlçeleri
('660e8400-e29b-41d4-a716-446655440006', 'Çankaya', true, '550e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440007', 'Keçiören', true, '550e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440008', 'Yenimahalle', true, '550e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),

-- İzmir İlçeleri
('660e8400-e29b-41d4-a716-446655440009', 'Konak', true, '550e8400-e29b-41d4-a716-446655440003', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440010', 'Karşıyaka', true, '550e8400-e29b-41d4-a716-446655440003', NOW(), NOW()),
('660e8400-e29b-41d4-a716-446655440011', 'Bornova', true, '550e8400-e29b-41d4-a716-446655440003', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Kategoriler (Categories)
INSERT INTO categories (id, name, description, icon, active, parent_id, created_at, updated_at) VALUES
('770e8400-e29b-41d4-a716-446655440001', 'Emlak', 'Ev, arsa, işyeri alım satım', 'home', true, NULL, NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440002', 'Araç', 'Otomobil, motosiklet, tekne', 'car', true, NULL, NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440003', 'Elektronik', 'Bilgisayar, telefon, beyaz eşya', 'laptop', true, NULL, NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440004', 'Ev & Bahçe', 'Mobilya, dekorasyon, bahçe', 'sofa', true, NULL, NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440005', 'Giyim & Aksesuar', 'Kadın, erkek, çocuk giyim', 'shirt', true, NULL, NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440006', 'İş Makineleri', 'Sanayi, tarım, inşaat', 'truck', true, NULL, NOW(), NOW()),

-- Alt kategoriler
('770e8400-e29b-41d4-a716-446655440007', 'Konut', 'Daire, villa, müstakil ev', 'home', true, '770e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440008', 'Arsa', 'Konut arsası, tarla, arazi', 'map', true, '770e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440009', 'Otomobil', 'Binek araç alım satım', 'car', true, '770e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
('770e8400-e29b-41d4-a716-446655440010', 'Motosiklet', 'Motor, scooter, ATV', 'motorcycle', true, '770e8400-e29b-41d4-a716-446655440002', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Test kullanıcısı (şifre: 123456)
INSERT INTO users (id, email, password, phone, first_name, last_name, status, role, email_verified, phone_verified, created_at, updated_at) VALUES
('880e8400-e29b-41d4-a716-446655440001', 'test@findo.com', '$2a$10$N6YjjM4qCqJGVhGhPOYy4.JQJ0QVY5V5K5J5K5J5K5J5K5J5K5J5K', '+905551234567', 'Test', 'User', 'ACTIVE', 'USER', true, true, NOW(), NOW()),
('880e8400-e29b-41d4-a716-446655440002', 'admin@findo.com', '$2a$10$N6YjjM4qCqJGVhGhPOYy4.JQJ0QVY5V5K5J5K5J5K5J5K5J5K5J5K', '+905551234568', 'Admin', 'User', 'ACTIVE', 'ADMIN', true, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Örnek ilanlar
INSERT INTO ads (id, title, description, price, status, user_id, category_id, city_id, district_id, created_at, updated_at) VALUES
('990e8400-e29b-41d4-a716-446655440001', 'Satılık 3+1 Daire - Kadıköy', 'Merkezi konumda, deniz manzaralı, asansörlü binada 3+1 daire', 850000.00, 'ACTIVE', '880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('990e8400-e29b-41d4-a716-446655440002', 'Temiz Volkswagen Golf', '2020 model, az kullanılmış, hasarsız Golf', 420000.00, 'ACTIVE', '880e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
('990e8400-e29b-41d4-a716-446655440003', 'iPhone 14 Pro Max 256GB', 'Sıfır ayarında, kutu ve aksesuarları tam', 45000.00, 'ACTIVE', '880e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440006', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- İstatistikler için bazı favoriler
INSERT INTO favorites (id, user_id, ad_id, created_at, updated_at) VALUES
('aa0e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440003', NOW(), NOW()),
('aa0e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440001', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
