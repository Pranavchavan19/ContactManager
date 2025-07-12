# === Stage 1: Node + Tailwind ===
FROM node:20-alpine AS tailwind-builder

WORKDIR /app

COPY package.json tailwind.config.js ./
COPY src/main/resources/static/css ./src/main/resources/static/css

RUN npm install

RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# === Stage 2: Build Spring Boot JAR ===
FROM maven:3.9.6-eclipse-temurin AS maven-builder
# ✅ Valid image: https://hub.docker.com/_/maven

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

# === Final Stage: Run the App ===
FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY --from=maven-builder /app/target/*.jar app.jar
COPY --from=tailwind-builder /app/src/main/resources/static/css/output.css /app/static/css/output.css

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
