#!/bin/bash

echo "Building FINDO API for Render deployment..."

# Build the Java application
echo "Compiling Java application..."
mvn clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Java build completed successfully"
    
    # Check if JAR file exists
    JAR_FILE=$(find target -name "*.jar" -not -name "*.jar.original" | head -1)
    if [ -n "$JAR_FILE" ]; then
        echo "✅ JAR file created: $JAR_FILE"
        echo "📦 Build ready for deployment"
    else
        echo "❌ JAR file not found"
        exit 1
    fi
else
    echo "❌ Java build failed"
    exit 1
fi

echo "🚀 Ready for Render deployment!"
