# -------------------
# Stage 1: Tailwind CSS build
# -------------------
FROM node:20-alpine AS tailwind

WORKDIR /app

# Install Tailwind dependencies
COPY package.json package-lock.json* ./
RUN npm install

# Copy Tailwind config and project source
COPY tailwind.config.js ./
COPY src ./src

# Build Tailwind CSS
RUN npm run build:css

# -------------------
# Stage 2: Spring Boot app build and run
# -------------------
FROM eclipse-temurin:20-jdk

WORKDIR /app

# Copy built JAR file
COPY dist/scm2.0-0.0.1-SNAPSHOT.jar .

# Copy compiled Tailwind CSS from previous stage
COPY --from=tailwind /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "scm2.0-0.0.1-SNAPSHOT.jar"]
