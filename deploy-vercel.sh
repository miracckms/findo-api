#!/bin/bash

# FINDO API Vercel Deployment Script
echo "🚀 Starting FINDO API Vercel Deployment..."
echo "📝 Note: Using Node.js wrapper approach for Java deployment"

# Check if Vercel CLI is installed
if ! command -v vercel &> /dev/null; then
    echo "📦 Installing Vercel CLI..."
    npm install -g vercel
fi

# Install Node.js dependencies
echo "📦 Installing Node.js dependencies..."
npm install

if [ $? -ne 0 ]; then
    echo "❌ Node.js dependencies installation failed!"
    exit 1
fi

# Build the Java application
echo "📦 Building Java application..."
./build.sh

if [ $? -ne 0 ]; then
    echo "❌ Java build failed!"
    exit 1
fi

# Verify JAR file exists
JAR_FILE=$(find target -name "*.jar" -not -name "*.jar.original" | head -1)
if [ -z "$JAR_FILE" ]; then
    echo "❌ JAR file not found in target directory!"
    exit 1
fi
echo "✅ JAR file found: $JAR_FILE"

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
    echo "4. Test the deployment with health check endpoint"
    echo ""
    echo "🔗 Important Environment Variables to set:"
    echo "   - DATABASE_URL"
    echo "   - DB_USERNAME"
    echo "   - DB_PASSWORD"
    echo "   - JWT_SECRET"
    echo "   - MAIL_USERNAME"
    echo "   - MAIL_PASSWORD"
    echo "   - CORS_ORIGINS"
    echo ""
    echo "🔍 Test your deployment:"
    echo "   - Health check: https://your-app.vercel.app/api/health"
    echo "   - Categories: https://your-app.vercel.app/api/categories"
    echo ""
    echo "💡 Note: First request may take longer due to Java app startup (cold start)"
else
    echo "❌ Deployment failed!"
    exit 1
fi
