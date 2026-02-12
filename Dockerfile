# Build stage
FROM maven:3.8.4-eclipse-temurin-8 AS builder

WORKDIR /app

ENV MAVEN_OPTS="-Xmx512m -Xms256m"

COPY pom.xml .
RUN mvn dependency:go-offline -B -Dmaven.compiler.fork=true

COPY src ./src

RUN mvn clean package -DskipTests -B -Dmaven.compiler.fork=true -Dmaven.compiler.meminitial=256m -Dmaven.compiler.maxmem=512m
# Runtime stage - run Spring Boot executable WAR directly
FROM eclipse-temurin:8-jre-jammy

WORKDIR /app

ENV JAVA_OPTS="-Xms128m -Xmx256m"

COPY --from=builder /app/target/ROOT.war /app/app.war

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.war"]
