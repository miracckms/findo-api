# Use OpenJDK 11 as base image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better Docker layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Install Maven and dependencies
RUN apt-get update && apt-get install -y curl && \
    chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/findo-api-0.0.1-SNAPSHOT.jar"]
