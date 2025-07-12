# 1️⃣ Base image with Node to build Tailwind CSS
FROM node:20-alpine AS tailwind-builder

WORKDIR /build

# Install Tailwind dependencies
COPY package.json ./
COPY tailwind.config.js ./
RUN npm install

# Copy source files
COPY src ./src

# Build Tailwind CSS
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# 2️⃣ Final image for Spring Boot
FROM eclipse-temurin:20-jdk

WORKDIR /app

# Copy Spring Boot JAR
COPY dist/scm2.0-0.0.1-SNAPSHOT.jar app.jar

# Copy Tailwind CSS output from previous stage
COPY --from=tailwind-builder /build/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
