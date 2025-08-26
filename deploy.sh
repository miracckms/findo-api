#!/bin/bash

# FINDO API Deployment Script
echo "🚀 Starting FINDO API Deployment..."

# Build the application
echo "📦 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Build Docker image
echo "🐳 Building Docker image..."
docker build -t findo-api:latest .

if [ $? -ne 0 ]; then
    echo "❌ Docker build failed!"
    exit 1
fi

echo "✅ Deployment preparation completed!"
echo ""
echo "📋 Next Steps:"
echo "1. For local testing: docker-compose up -d"
echo "2. For cloud deployment: docker push findo-api:latest"
echo "3. For production: Update environment variables in docker-compose.yml"
echo ""
echo "🔗 Endpoints will be available at:"
echo "   - API: http://localhost:8080/api"
echo "   - Health: http://localhost:8080/api/actuator/health"
