#!/bin/bash

# FINDO API Vercel Deployment Script
echo "🚀 Starting FINDO API Vercel Deployment..."

# Check if Vercel CLI is installed
if ! command -v vercel &> /dev/null; then
    echo "📦 Installing Vercel CLI..."
    npm install -g vercel
fi

# Build the application
echo "📦 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Login to Vercel (if not already logged in)
echo "🔐 Checking Vercel login status..."
vercel whoami || vercel login

# Deploy to Vercel
echo "🚀 Deploying to Vercel..."
vercel --prod

if [ $? -eq 0 ]; then
    echo "✅ Deployment successful!"
    echo ""
    echo "📋 Next Steps:"
    echo "1. Configure environment variables in Vercel dashboard"
    echo "2. Set up your database (PlanetScale, Railway, or Supabase)"
    echo "3. Update CORS origins in environment variables"
    echo ""
    echo "🔗 Important Environment Variables to set:"
    echo "   - DATABASE_URL"
    echo "   - DB_USERNAME"
    echo "   - DB_PASSWORD"
    echo "   - JWT_SECRET"
    echo "   - MAIL_USERNAME"
    echo "   - MAIL_PASSWORD"
    echo "   - CORS_ORIGINS"
else
    echo "❌ Deployment failed!"
    exit 1
fi
