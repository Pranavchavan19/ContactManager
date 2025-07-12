# ===== Stage 1: Build Spring Boot Application =====
FROM maven:3.9.6-eclipse-temurin-17 AS maven-builder

WORKDIR /app

# Copy everything and build the JAR (skip tests for speed)
COPY . .
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Final runtime image =====
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from builder
COPY --from=maven-builder /app/target/*.jar app.jar

# Expose port (change if different)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
