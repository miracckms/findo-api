# 🚀 FINDO API - Vercel Deployment Guide

> **⚠️ IMPORTANT UPDATE**: Vercel has deprecated Java runtime support. This guide now uses a Node.js wrapper approach to deploy your Java Spring Boot application.

## Prerequisites

1. **Node.js** (v18+) and **npm** installed
2. **Maven** (for building Java application)
3. **Java 11+** (for compilation)
4. **Vercel CLI** installed: `npm install -g vercel`
5. **Vercel Account** (free tier available)
6. **MySQL Database** (PlanetScale, Railway, or Supabase recommended)

## 📁 Project Structure for Vercel

```
findo-api/
├── api/
│   └── index.js               # Node.js wrapper for Java app
├── src/main/java/...          # Java source code
├── src/main/resources/...     # Application properties
├── target/                    # Compiled Java application
│   └── *.jar                  # Spring Boot JAR file
├── pom.xml                    # Maven configuration
├── vercel.json                # Vercel configuration (Node.js runtime)
├── package.json               # Node.js dependencies
├── build.sh                   # Build script
└── deploy-vercel.sh          # Deployment script
```

## 🔧 How It Works

The new deployment approach uses a Node.js wrapper that:

1. **Builds** your Java application using Maven
2. **Starts** the Spring Boot JAR file when a request comes in
3. **Proxies** all HTTP requests to the running Java application
4. **Manages** the lifecycle of the Java process in Vercel's serverless environment

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

### Step 1: Build Your Java Application

```bash
# Build the Java application first
npm run build
# or
./build.sh

# This will create the JAR file in target/ directory
```

### Step 2: Prepare Your Repository

```bash
# Ensure your code is in Git
git add .
git commit -m "Prepare for Vercel deployment with Node.js wrapper"
git push origin main
```

### Step 3: Install Vercel CLI

```bash
npm install -g vercel
```

### Step 4: Install Node.js Dependencies

```bash
npm install
```

### Step 5: Login to Vercel

```bash
vercel login
```

### Step 6: Configure Environment Variables

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

### Step 7: Deploy

```bash
# Make sure JAR file is built first
npm run build

# Then deploy
vercel --prod

# Or use the deployment script
./deploy-vercel.sh
```

> **💡 Important**: The JAR file must be included in your Git repository for Vercel deployment to work, as Vercel cannot run Maven during deployment.

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

- **builds**: Specifies Node.js runtime with `api/index.js` entry point
- **routes**: Routes all requests to the Node.js wrapper
- **functions**: Configures memory (1024MB) and timeout (30s)

### api/index.js

- **Node.js wrapper** that manages the Java application lifecycle
- **Proxy function** that forwards HTTP requests to Spring Boot
- **Health checks** and error handling
- **CORS configuration** for cross-origin requests

### application-vercel.properties

- Optimized for serverless environment
- Reduced connection pool size (5 max, 1 min)
- Shorter timeouts for faster cold starts
- Minimal logging for better performance

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

### Issue 1: "@vercel/java not found" Error

**✅ Solution:** This guide fixes this issue by using Node.js wrapper instead of deprecated Java runtime.

### Issue 2: JAR File Not Found

**Solution:**

```bash
# Build the application first
npm run build
# Ensure JAR file is committed to Git
git add target/*.jar
git commit -m "Add JAR file for deployment"
```

### Issue 3: Java Application Startup Timeout

**Solution:**

- Reduce application startup time by optimizing Spring Boot configuration
- Increase timeout in `vercel.json` if necessary
- Use `application-vercel.properties` for faster cold starts

### Issue 4: Database Connection Issues

**Solution:** Check connection string format and SSL requirements in `application-vercel.properties`

### Issue 5: Function Memory Limits

**Solution:** Increase memory allocation in `vercel.json` functions configuration (up to 1024MB on Pro plan)

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
