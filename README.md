# Findo API - Classified Ads Platform

Findo, kullanıcıların ürünlerini satabilecekleri ve başka kullanıcıların ürünleri görüntüleyip iletişime geçebilecekleri bir sınıflandırılmış ilan platformudur.

## Özellikler

### 👥 Kullanıcı Rolleri

- **Ziyaretçi (Anonim)**: İlanları görüntüleyebilir, arama/filtreleme yapabilir
- **Üye**: İlan oluşturabilir, favorilere ekleyebilir, profil yönetimi yapabilir
- **Admin**: İlan moderasyonu, kullanıcı yönetimi

### 🔍 Ana Özellikler

- **Arama & Filtreleme**: Kategori, şehir, ilçe, fiyat aralığı, sıralama
- **İlan Yönetimi**: Oluşturma, düzenleme, yayına alma, moderasyon
- **Kullanıcı Sistemi**: E-posta/telefon ile kayıt, OTP doğrulama
- **Dosya Yükleme**: Görsel yükleme, otomatik sıkıştırma/thumbnail
- **Favoriler**: Üyeler için server-side, ziyaretçiler için local storage
- **Bildirimler**: E-posta bildirimleri (ilan onayı/reddi)

## 🛠 Teknoloji Stack

- **Java 11**
- **Spring Boot 2.7.18**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **H2 Database** (Development)
- **MySQL 8.0** (Production)
- **Maven**
- **Swagger/OpenAPI 3** (API Documentation)
- **Thumbnailator** (Görsel işleme)
- **UUID** (Unique Identifiers)

## 📋 Gereksinimler

- Java 11+
- MySQL 8.0+
- Maven 3.6+

## 🚀 Kurulum

### 1. Projeyi Klonlayın

```bash
git clone <repository-url>
cd findo-api
```

### 2. MySQL Veritabanı Oluşturun

```sql
CREATE DATABASE findo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'findo_user'@'localhost' IDENTIFIED BY 'findo_password';
GRANT ALL PRIVILEGES ON findo_db.* TO 'findo_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Uygulama Özelliklerini Yapılandırın

`src/main/resources/application.yml` dosyasında gerekli ayarları yapın:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/findo_db
    username: findo_user
    password: findo_password

  mail:
    host: smtp.gmail.com
    username: your-email@gmail.com
    password: your-app-password
```

### 4. Uygulamayı Çalıştırın

```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8080/api` adresinde çalışacaktır.

### 5. Swagger UI'ya Erişim

API dokümantasyonu için: http://localhost:8080/api/swagger-ui/index.html

### 6. H2 Console (Development)

Development ortamında H2 veritabanı kullanıyorsanız:

- **URL**: http://localhost:8080/api/h2-console
- **JDBC URL**: `jdbc:h2:mem:findo_db`
- **Username**: `sa`
- **Password**: (boş)

## 📚 API Endpoints

### 🔐 Authentication

- `POST /auth/login` - Giriş yap
- `POST /auth/register` - Kayıt ol
- `POST /auth/verify-email?token=` - E-posta doğrula
- `POST /auth/verify-phone?token=` - Telefon doğrula
- `GET /auth/me` - Mevcut kullanıcı bilgileri
- `PUT /auth/profile` - Profil güncelle

### 📢 İlanlar

- `GET /ads/search` - İlan arama/filtreleme
- `GET /ads/{id}` - İlan detayı
- `POST /ads` - İlan oluştur
- `PUT /ads/{id}` - İlan güncelle
- `DELETE /ads/{id}` - İlan sil
- `POST /ads/{id}/submit` - İlanı onaya gönder
- `GET /ads/my-ads` - Kullanıcının ilanları
- `GET /ads/featured` - Öne çıkan ilanlar
- `GET /ads/all` - Tüm aktif ilanlar (pagination destekli)

### 📁 Dosya Yükleme

- `POST /uploads/images` - Görsel yükle
- `GET /uploads/**` - Görsel göster
- `DELETE /uploads/images?imagePath=` - Görsel sil

### 🏷 Kategoriler

- `GET /categories` - Tüm kategoriler
- `GET /categories/root` - Ana kategoriler
- `GET /categories/{id}/children` - Alt kategoriler
- `GET /categories/{id}` - Kategori detayı

### 🌍 Lokasyon

- `GET /locations/cities` - Şehirler
- `GET /locations/cities/{id}/districts` - İlçeler
- `GET /locations/districts/{id}` - İlçe detayı

### 👑 Admin (Yalnızca Admin)

- `GET /admin/ads/pending` - Onay bekleyen ilanlar
- `POST /admin/ads/{id}/approve` - İlanı onayla
- `POST /admin/ads/{id}/reject` - İlanı reddet
- `GET /admin/stats` - İstatistikler

## 🔧 Yapılandırma

### Environment Variables

Aşağıdaki ortam değişkenlerini ayarlayabilirsiniz:

```bash
DB_USERNAME=findo_user
DB_PASSWORD=findo_password
JWT_SECRET=your-jwt-secret-key
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
FILE_UPLOAD_DIR=./uploads
```

### Dosya Yükleme

- Maksimum dosya boyutu: 10MB
- Desteklenen formatlar: JPG, JPEG, PNG, GIF, WebP
- Otomatik thumbnail oluşturma
- Görsel sıkıştırma (1200x800 max)

## 👥 Varsayılan Kullanıcılar

Uygulama ilk çalıştırıldığında aşağıdaki kullanıcılar oluşturulur:

**Admin:**

- E-posta: admin@findo.com
- Şifre: admin123

**Test Kullanıcı:**

- E-posta: test@findo.com
- Telefon: +905551234567
- Şifre: test123

## 🏗 Proje Yapısı

```
src/main/java/com/findo/
├── config/          # Yapılandırma sınıfları
├── controller/      # REST Controller'lar
├── dto/            # Data Transfer Objects
├── model/          # JPA Entity'ler
├── repository/     # JPA Repository'ler
├── security/       # Security yapılandırması
└── service/        # Business Logic
```

## 🔄 Geliştirme

### Veritabanı Şeması

JPA otomatik olarak tabloları oluşturur. Manuel kontrol için `spring.jpa.hibernate.ddl-auto=validate` ayarını kullanabilirsiniz.

### Test

```bash
mvn test
```

### Build

```bash
mvn clean package
```

## 📞 Destek

Sorularınız için issue oluşturun veya [email] ile iletişime geçin.

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
