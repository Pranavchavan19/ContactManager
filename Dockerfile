# 🌱 Stage 1: Build Tailwind CSS and Spring Boot JAR
FROM eclipse-temurin:20-jdk-jammy as builder

RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm

WORKDIR /app

COPY . .

RUN npm install -D tailwindcss

# ✅ Add execute permission to tailwind binary
RUN chmod +x node_modules/.bin/tailwindcss

# ✅ Now build CSS
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

RUN ./mvnw clean package -DskipTests

# 🏁 Final runtime image
FROM eclipse-temurin:20-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
