# ========== STEP 1: Build Tailwind CSS ==========
FROM node:20-alpine AS tailwind

WORKDIR /app

# Copy only what's needed to build CSS
COPY package.json tailwind.config.js ./
COPY src ./src

RUN npm install
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# ========== STEP 2: Build Spring Boot ==========
FROM maven:3.9.6-eclipse-temurin-20-alpine AS build

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# ========== STEP 3: Final Runtime ==========
FROM eclipse-temurin:20-jdk

WORKDIR /app

# Copy final JAR
COPY --from=build /app/target/*.jar app.jar

# 🔥 Copy freshly built Tailwind output.css
COPY --from=tailwind /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
