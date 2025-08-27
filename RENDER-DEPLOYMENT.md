# FINDO API - Render Deployment Guide

Bu guide PostgreSQL veritabanı ile Render'da FINDO API'sini deploy etme sürecini açıklar.

## 📋 Ön Gereksinimler

- GitHub repository'si
- Render hesabı (ücretsiz)

## 🗄️ Adım 1: PostgreSQL Veritabanı Oluşturma

1. **Render Dashboard**'a giriş yapın
2. **"New +"** butonuna tıklayın
3. **"PostgreSQL"** seçin
4. Aşağıdaki bilgileri doldurun:

   - **Name**: `findo-db`
   - **Database**: `findo_db`
   - **User**: `findo_user`
   - **Region**: Size en yakın region
   - **PostgreSQL Version**: 15 (önerilen)
   - **Plan**: **Free** seçin

5. **"Create Database"** butonuna tıklayın
6. Veritabanı oluşturulduktan sonra **"Info"** sekmesinden connection bilgilerini kopyalayın:
   - **Internal Database URL**
   - **External Database URL**
   - **Host**, **Port**, **Database**, **Username**, **Password**

> ⚠️ **Önemli**: Free PostgreSQL 1 GB kapasiteye sahip ve 30 gün sonra upgrade gerektirir.

## 🚀 Adım 2: Web Service Oluşturma

1. Render Dashboard'da **"New +"** → **"Web Service"** seçin
2. GitHub repository'nizi bağlayın: `your-username/FINDO-API`
3. Aşağıdaki ayarları yapın:

### Temel Ayarlar

- **Name**: `findo-api`
- **Region**: PostgreSQL ile aynı region
- **Branch**: `main`
- **Runtime**: `Docker` VEYA `Native`

### Build & Deploy Ayarları

#### Option A: Native Environment (Önerilen)

- **Build Command**: `./mvnw clean package -DskipTests`
- **Start Command**: `java -jar target/findo-api-0.0.1-SNAPSHOT.jar`

#### Option B: Docker

- **Build Command**: (boş bırakın)
- **Start Command**: (boş bırakın)

### Plan

- **Instance Type**: **Free** seçin

## 🔧 Adım 3: Environment Variables Ayarlama

Web Service oluşturulduktan sonra **"Environment"** sekmesine gidin ve aşağıdaki değişkenleri ekleyin:

### Zorunlu Değişkenler (PostgreSQL'den kopyalayın)

```bash
DB_HOST=dpg-xxxxxxxxxx-a.oregon-postgres.render.com
DB_PORT=5432
DB_NAME=findo_db
DB_USER=findo_user
DB_PASS=your_generated_password
```

### JWT Konfigürasyonu

```bash
JWT_SECRET=your_super_secure_jwt_secret_key_here_at_least_32_characters_long
```

### Opsiyonel Email Ayarları

```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

### Opsiyonel SMS Ayarları

```bash
SMS_PROVIDER=twilio
SMS_ACCOUNT_SID=your_twilio_sid
SMS_AUTH_TOKEN=your_twilio_token
SMS_FROM_NUMBER=+1234567890
```

### Diğer Ayarlar

```bash
FILE_UPLOAD_DIR=/tmp/uploads
ADMIN_EMAILS=admin@yoursite.com
SPRING_PROFILES_ACTIVE=prod
```

## 📝 Adım 4: Deploy

1. Tüm environment variables ayarlandıktan sonra **"Create Web Service"** butonuna tıklayın
2. Build process başlayacak (5-10 dakika sürebilir)
3. Deploy tamamlandığında size bir URL verilecek: `https://findo-api.onrender.com`

## 🔍 Adım 5: Test Etme

Deploy tamamlandıktan sonra:

1. **Health Check**: `https://findo-api.onrender.com/api/actuator/health`
2. **API Documentation**: `https://findo-api.onrender.com/api/swagger-ui.html`
3. **Test Endpoint**: `https://findo-api.onrender.com/api/auth/test`

## 🚨 Sorun Giderme

### Build Hatası

- **Logs** sekmesinden build loglarını kontrol edin
- Java version uyumluluğunu kontrol edin (Java 11)
- Maven wrapper (`./mvnw`) executable olmalı

### Database Connection Hatası

- Environment variables doğru olmalı
- PostgreSQL instance çalışır durumda olmalı
- Network bağlantısını kontrol edin

### Memory/Performance Issues

- Free plan 512 MB RAM sağlar
- Gerekirse Starter plan'a upgrade yapın ($7/ay)

## 💡 Optimizasyon Önerileri

### Production için

1. **Starter Plan** kullanın (daha stabil, auto-sleep yok)
2. **PostgreSQL Paid Plan** kullanın (persistent, backup)
3. **Custom Domain** ekleyin
4. **SSL Certificate** otomatik gelir

### Monitoring

- Render otomatik health check yapar
- **Metrics** sekmesinden performance takibi
- **Logs** real-time log monitoring

### Backup Strategy

- Free PostgreSQL backup sunmaz
- Production için paid plan gerekli
- Manuel export/import için `pg_dump` kullanın

## 🔗 Faydalı Linkler

- [Render Documentation](https://render.com/docs)
- [PostgreSQL on Render](https://render.com/docs/databases)
- [Environment Variables](https://render.com/docs/environment-variables)
- [Custom Domains](https://render.com/docs/custom-domains)

## 📞 Destek

Sorun yaşarsanız:

1. Render logs kontrol edin
2. GitHub Issues açın
3. Render Community'ye başvurun

---

## 🎯 Sonraki Adımlar

Deploy başarılı olduktan sonra:

- [ ] Frontend'i bu API endpoint'e bağlayın
- [ ] Production database'i seed edin
- [ ] Email/SMS servislerini test edin
- [ ] Performance monitoring kurun
- [ ] Backup stratejisi oluşturun

**Not**: Free tier 30 günlük PostgreSQL limiti vardır. Production kullanım için upgrade planlayın.
