# ========== Stage 1: Tailwind CSS Build ==========
FROM node:20-alpine AS tailwind-builder

WORKDIR /app

# Only copy necessary files to install and run Tailwind
COPY package.json package-lock.json* tailwind.config.js ./
COPY src/main/resources/static/css ./src/main/resources/static/css
COPY src/main/resources/templates ./src/main/resources/templates

RUN npm install
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# ========== Stage 2: Maven Build ==========
FROM maven:3.9.6-eclipse-temurin-21 AS maven-builder

WORKDIR /app

# Copy everything
COPY . .

# Fix: Make mvnw executable
RUN chmod +x mvnw

# ✅ Run Maven clean package (this is the correct command)
RUN ./mvnw clean package -DskipTests

# ========== Final Stage ==========
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy built jar
COPY --from=maven-builder /app/target/*.jar app.jar

# Copy generated output.css from tailwind build
COPY --from=tailwind-builder /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
