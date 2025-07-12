# =================== Stage 1: Tailwind CSS Build ===================
FROM node:20-alpine AS tailwind-builder

WORKDIR /app

# Copy only necessary files for npm install and Tailwind build
COPY package.json package-lock.json* tailwind.config.js ./
COPY src/main/resources/static/css ./src/main/resources/static/css
COPY src/main/resources/templates ./src/main/resources/templates

# Install Tailwind and build CSS
RUN npm install
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify


# =================== Stage 2: Maven Build ===================
FROM maven:3.9-eclipse-temurin-21-alpine AS maven-builder

WORKDIR /app

# Copy the full project
COPY . .

# Make Maven Wrapper executable (if you're using it)
RUN chmod +x ./mvnw

# Build the Spring Boot project (skip tests for faster build)
RUN ./mvnw clean package -DskipTests


# =================== Final Stage: Runtime ===================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy built JAR from previous stage
COPY --from=maven-builder /app/target/*.jar app.jar

# Copy the generated CSS file into final image
COPY --from=tailwind-builder /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

# Expose the app port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
