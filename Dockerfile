# Build stage
FROM maven:3.8.4-eclipse-temurin-8 AS builder

WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build WAR application
RUN mvn clean package -DskipTests

# Runtime stage - using Tomcat for WAR deployment
FROM tomcat:9.0-jre8-alpine

# Install curl for health check
RUN apk add --no-cache curl

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy built WAR from builder stage
COPY --from=builder /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1
