# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]









# FROM openjdk:17-jdk-slim

# WORKDIR /app

# COPY target/*.jar app.jar

# ENV SPRING_PROFILES_ACTIVE=prod
# ENV SERVER_PORT=8080

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "app.jar"]
