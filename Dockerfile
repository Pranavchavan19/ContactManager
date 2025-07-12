# 🌱 Stage 1: Build Tailwind CSS and Spring Boot JAR
FROM eclipse-temurin:20-jdk-jammy as builder

# Install curl, node, npm (needed for Tailwind)
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Install Tailwind
RUN npm install -D tailwindcss

# Build Tailwind CSS
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# Build Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# 🏁 Stage 2: Run app in slim image
FROM eclipse-temurin:20-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
