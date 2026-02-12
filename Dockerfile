# Build stage - optimized for low memory
FROM maven:3.8.4-eclipse-temurin-8 AS builder

WORKDIR /app

# Set memory limits for Maven build
ENV MAVEN_OPTS="-Xmx512m -Xms256m"

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies with limited memory
RUN mvn dependency:go-offline -B -Dmaven.compiler.fork=true

# Copy source code
COPY src ./src

# Build with limited memory
RUN mvn clean package -DskipTests -B -Dmaven.compiler.fork=true -Dmaven.compiler.meminitial=256m -Dmaven.compiler.maxmem=512m

# Runtime stage - using Tomcat for WAR deployment
FROM tomcat:9.0-jre8-alpine

# Install curl for health check
RUN apk add --no-cache curl

# Set memory limits for Tomcat
ENV CATALINA_OPTS="-Xmx256m -Xms128m"
ENV JAVA_OPTS="-Xmx256m -Xms128m"

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy built WAR from builder stage
COPY --from=builder /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1
