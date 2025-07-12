# ---------- Stage 1: Build App ----------
FROM maven:3.9.6-eclipse-temurin-21 AS maven-builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy entire project
COPY . .

# ✅ Install Tailwind CSS + generate output.css
RUN apt-get update && apt-get install -y npm && \
    npm install -D tailwindcss postcss autoprefixer && \
    chmod +x node_modules/.bin/tailwindcss && \
    npx tailwindcss -i ./src/main/resources/static/input.css -o ./src/main/resources/static/output.css --minify

# ✅ Build Spring Boot app (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run App ----------
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy jar from builder stage
COPY --from=maven-builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
