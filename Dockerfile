# ===== Stage 1: Build the JAR =====
FROM maven:3.9.6-eclipse-temurin-17 AS maven-builder

WORKDIR /app

# Only copy needed files to avoid cache issues
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# ===== Stage 2: Run the JAR =====
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=maven-builder /app/target/*.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
