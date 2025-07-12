# ========== Stage 1: Build JAR + Tailwind ==========
FROM eclipse-temurin:21 AS maven-builder

WORKDIR /app

# Install Node.js and npm (required for Tailwind)
RUN apt-get update && apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm@latest

# Copy all project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Install Tailwind CSS and generate output.css
RUN npm install -D tailwindcss postcss autoprefixer && \
    npx tailwindcss -i ./src/main/resources/static/input.css -o ./src/main/resources/static/output.css --minify

# Build the Spring Boot app
RUN ./mvnw clean package -DskipTests


# ========== Stage 2: Run the App ==========
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=maven-builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
