# 🚀 FINDO API - Vercel Deployment Guide

## Prerequisites

1. **Node.js** (v18+) and **npm** installed
2. **Vercel CLI** installed: `npm install -g vercel`
3. **Vercel Account** (free tier available)
4. **MySQL Database** (PlanetScale, Railway, or Supabase recommended)

## 📁 Project Structure for Vercel

```
findo-api/
├── src/main/java/...           # Java source code
├── src/main/resources/...      # Application properties
├── pom.xml                     # Maven configuration
├── vercel.json                 # Vercel configuration
├── package.json                # Node.js dependencies
└── deploy-vercel.sh           # Deployment script
```

## 🗄️ Database Setup Options

### Option 1: PlanetScale (Recommended)
```bash
# 1. Create account at planetscale.com
# 2. Create database
# 3. Get connection string
# 4. Import your schema and data
```

### Option 2: Railway
```bash
# 1. Create account at railway.app
# 2. Create MySQL database
# 3. Get connection details
# 4. Import your schema
```

### Option 3: Supabase
```bash
# 1. Create account at supabase.com
# 2. Create project
# 3. Use built-in PostgreSQL (modify dialect)
```

## 🚀 Deployment Steps

### Step 1: Prepare Your Repository

```bash
# Ensure your code is in Git
git add .
git commit -m "Prepare for Vercel deployment"
git push origin main
```

### Step 2: Install Vercel CLI

```bash
npm install -g vercel
```

### Step 3: Login to Vercel

```bash
vercel login
```

### Step 4: Configure Environment Variables

Create the following environment variables in Vercel dashboard:

**Required Variables:**
- `DATABASE_URL`: Your MySQL connection string
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Strong secret key (256-bit)
- `SPRING_PROFILES_ACTIVE`: `vercel`

**Optional Variables:**
- `MAIL_HOST`: SMTP host
- `MAIL_USERNAME`: Email username  
- `MAIL_PASSWORD`: Email password
- `CORS_ORIGINS`: Your frontend URLs
- `SMS_API_KEY`: SMS service key
- `SMS_API_SECRET`: SMS service secret

### Step 5: Deploy

```bash
# Automatic deployment
./deploy-vercel.sh

# Or manual deployment
vercel --prod
```

## ⚙️ Environment Variables Setup

1. Go to your Vercel project dashboard
2. Navigate to **Settings** → **Environment Variables**
3. Add each variable from `vercel-env-template.txt`

### Example Environment Variables:

```
DATABASE_URL=mysql://user:pass@aws.connect.psdb.cloud/findo_db?sslMode=REQUIRED
DB_USERNAME=username
DB_PASSWORD=pscale_pw_xxx
JWT_SECRET=myVerySecretKeyForProductionThatShouldBe256BitsLongForSecurityPurposes
CORS_ORIGINS=https://your-frontend.vercel.app
SPRING_PROFILES_ACTIVE=vercel
```

## 🔧 Vercel Configuration Explained

### vercel.json
- **builds**: Specifies Java runtime
- **routes**: Handles API routing
- **functions**: Configures memory and timeout

### application-vercel.properties
- Optimized for serverless environment
- Reduced connection pool size
- Temporary file handling for uploads

## 📊 Testing Your Deployment

After deployment, test these endpoints:

```bash
# Health check
curl https://your-app.vercel.app/api/actuator/health

# Categories
curl https://your-app.vercel.app/api/categories

# Authentication
curl -X POST https://your-app.vercel.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@findo.com","password":"admin123"}'
```

## 🚨 Common Issues & Solutions

### Issue 1: Database Connection Timeout
**Solution:** Check connection string format and SSL requirements

### Issue 2: Environment Variables Not Working
**Solution:** Ensure variables are set in Vercel dashboard, not local .env

### Issue 3: Function Timeout
**Solution:** Optimize database queries and increase timeout in vercel.json

### Issue 4: File Upload Issues
**Solution:** Use cloud storage (AWS S3, Cloudinary) instead of local files

## 🔒 Security Considerations

1. **JWT Secret**: Use a strong, unique secret
2. **Database**: Enable SSL connections
3. **CORS**: Specify exact frontend domains
4. **Environment Variables**: Never commit secrets to Git

## 📈 Monitoring & Logs

1. **Vercel Dashboard**: Monitor function executions
2. **Function Logs**: Check runtime logs in Vercel
3. **Health Endpoint**: Use `/api/actuator/health` for monitoring

## 🆙 Updating Your App

```bash
# Make changes to your code
git add .
git commit -m "Update features"
git push origin main

# Redeploy
vercel --prod
```

## 💡 Tips for Serverless

1. **Connection Pooling**: Keep pool size small (5-10)
2. **Cold Starts**: Expect some initial latency
3. **File Storage**: Use external storage services
4. **Database**: Use connection pooling-friendly databases
5. **Caching**: Implement caching where possible

## 📞 Support

- **Vercel Docs**: https://vercel.com/docs
- **Java Runtime**: https://vercel.com/docs/runtimes/java
- **Environment Variables**: https://vercel.com/docs/concepts/projects/environment-variables
