# FINDO API Deployment Guide

## 🚀 Deployment Options

### 1. Local Docker Deployment

```bash
# Build and run locally
./deploy.sh
docker-compose up -d

# Check status
docker-compose ps
curl http://localhost:8080/api/actuator/health
```

### 2. Heroku Deployment

```bash
# Install Heroku CLI
# Login to Heroku
heroku login

# Create app
heroku create your-findo-api

# Add MySQL addon
heroku addons:create cleardb:ignite

# Set environment variables
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set JWT_SECRET=your-very-long-secret-key
heroku config:set MAIL_USERNAME=your-email@gmail.com
heroku config:set MAIL_PASSWORD=your-app-password

# Get database URL
heroku config:get CLEARDB_DATABASE_URL

# Deploy
git push heroku main
```

### 3. AWS Deployment (Docker)

```bash
# Build and tag for ECR
docker build -t findo-api .
docker tag findo-api:latest your-account.dkr.ecr.region.amazonaws.com/findo-api:latest

# Push to ECR
aws ecr get-login-password --region region | docker login --username AWS --password-stdin your-account.dkr.ecr.region.amazonaws.com
docker push your-account.dkr.ecr.region.amazonaws.com/findo-api:latest

# Deploy to ECS/Fargate or EB
```

### 4. DigitalOcean App Platform

```yaml
# app.yaml
name: findo-api
services:
  - name: api
    source_dir: /
    github:
      repo: your-username/findo-api
      branch: main
    run_command: java -jar target/findo-api-*.jar --spring.profiles.active=prod
    environment_slug: java
    instance_count: 1
    instance_size_slug: basic-xxs
    envs:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: DATABASE_URL
        value: ${db.DATABASE_URL}
      - key: JWT_SECRET
        value: your-jwt-secret
databases:
  - name: db
    engine: MYSQL
    version: "8"
```

## 🔧 Environment Variables

### Required Variables:

- `DATABASE_URL`: MySQL connection string
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing key (256-bit)

### Optional Variables:

- `MAIL_HOST`: SMTP host
- `MAIL_USERNAME`: Email username
- `MAIL_PASSWORD`: Email password
- `UPLOAD_DIR`: File upload directory
- `CORS_ORIGINS`: Allowed CORS origins

## ✅ Health Checks

- Health endpoint: `/api/actuator/health`
- Info endpoint: `/api/actuator/info`

## 📊 Monitoring

The application includes built-in health checks and logging for monitoring:

- Application logs are written to stdout
- Health check endpoint for load balancers
- Database connection monitoring
- JWT token validation logging

## 🔒 Security Considerations

1. Use strong JWT secret (256-bit)
2. Configure proper CORS origins
3. Use environment variables for sensitive data
4. Enable HTTPS in production
5. Use secure database connections
6. Regularly update dependencies

## 🗄️ Database Setup

1. Create MySQL database
2. Run schema creation scripts:
   - `database.sql` - Schema
   - `user_records_string.sql` - Initial data
3. Set `spring.jpa.hibernate.ddl-auto=validate` in production

## 📝 Logs

Application logs include:

- Request/Response logging
- Security events
- Database query logging (disabled in production)
- Error tracking
