# ===== Stage 1: Build Spring Boot App =====
FROM maven:3.9.6-eclipse-temurin-17 AS maven-builder

WORKDIR /app

# Copy all files
COPY . .

# ✅ Give execute permission to mvnw
RUN chmod +x mvnw

# ✅ Build the app
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from builder
COPY --from=maven-builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
