-- Basit Kullanıcı Kayıtları - Bu DB için
-- Bu dosyayı çalıştırmadan önce: USE findo_db;

-- Basit bir users tablosu oluştur (eğer daha karmaşık bir şema varsa çakışabilir)
CREATE TABLE IF NOT EXISTS simple_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Admin kullanıcı ekle
INSERT INTO simple_users (email, password, first_name, last_name, role, status) VALUES
('admin@findo.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'Admin', 'User', 'ADMIN', 'ACTIVE'),
('test@findo.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'Test', 'User', 'USER', 'ACTIVE'),
('user@findo.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi2', 'Normal', 'User', 'USER', 'ACTIVE');

-- Giriş Bilgileri:
-- admin@findo.com / secret
-- test@findo.com / secret  
-- user@findo.com / secret

-- Mevcut karmaşık tablolar için basit sample data:
DELETE FROM users WHERE email IN ('admin@findo.com', 'test@findo.com', 'user@findo.com');

-- Bu UUID formatında ID'ler için örnek kayıtlar
-- (UUID çalışırsa bu kısım kullanılabilir)

COMMIT;
