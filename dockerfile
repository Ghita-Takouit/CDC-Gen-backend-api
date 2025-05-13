FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file built by Maven
COPY target/*.jar app.jar

# Environment variables that can be overridden at runtime
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Copy the .env file with secrets (this will be created by Jenkins)
COPY .env /app/.env

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]