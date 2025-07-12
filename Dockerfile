# === Stage 1: Node + Tailwind ===
FROM node:20-alpine AS tailwind-builder

# Set workdir
WORKDIR /app

# Copy Tailwind config and input files
COPY package.json tailwind.config.js ./
COPY src/main/resources/static/css ./src/main/resources/static/css

# Install Tailwind
RUN npm install

# Build CSS
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# === Stage 2: Maven Build ===
FROM maven:3.9.6-eclipse-temurin-20 AS maven-builder

WORKDIR /app

# Copy all code and .mvn wrapper
COPY . .

# Build Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# === Final Stage: Run the JAR ===
FROM eclipse-temurin:20-jdk

WORKDIR /app

# Copy JAR from Maven build
COPY --from=maven-builder /app/target/*.jar app.jar

# Copy CSS output from Tailwind build
COPY --from=tailwind-builder /app/src/main/resources/static/css/output.css /app/static/css/output.css

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
