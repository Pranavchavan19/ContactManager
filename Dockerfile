# ========= Base Stage =========
FROM maven:3.9.6-eclipse-temurin-17 AS maven-builder

# Set working directory
WORKDIR /app

# Copy the Maven wrapper and project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# ========= Tailwind CSS Build =========
# Install Node and Tailwind CLI dependencies and build output.css
RUN apt-get update && apt-get install -y npm && \
    npm install tailwindcss postcss autoprefixer && \
    chmod +x node_modules/.bin/tailwindcss && \
    npx tailwindcss -i ./src/main/resources/static/input.css -o ./src/main/resources/static/output.css --minify

# ========= Build Java Project =========
RUN ./mvnw clean package -DskipTests

# ========= Final Production Image =========
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from builder image
COPY --from=maven-builder /app/target/*.jar app.jar

# Expose port (adjust if your Spring Boot app uses a different port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
