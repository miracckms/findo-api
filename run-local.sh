#!/bin/bash

echo "🚀 Starting FINDO API with Live PostgreSQL Database..."

# Check if .env.local exists
if [ ! -f ".env.local" ]; then
    echo "❌ .env.local file not found!"
    echo "📝 Please create .env.local file with your Render PostgreSQL credentials"
    echo "💡 Use local-env-template.txt as reference"
    exit 1
fi

# Load environment variables
echo "📦 Loading environment variables from .env.local..."
export $(cat .env.local | grep -v '^#' | xargs)

# Verify database connection variables
if [ -z "$DB_HOST" ] || [ -z "$DB_USER" ] || [ -z "$DB_PASS" ]; then
    echo "❌ Database connection variables missing!"
    echo "🔧 Please check DB_HOST, DB_USER, DB_PASS in .env.local"
    exit 1
fi

echo "✅ Environment variables loaded"
echo "🔗 Connecting to: $DB_HOST"

# Start the application
echo "🏃‍♂️ Starting Spring Boot application..."
mvn spring-boot:run

# Note: First run will create tables automatically (ddl-auto=update)
