# Multi-stage build for smaller final image
FROM openjdk:11-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better Docker layer caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and download dependencies
RUN chmod +x ./mvnw && \
    ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Final stage with JRE
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/findo-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
