# Use JDK 20
FROM openjdk:20

# Install Node.js (needed for Tailwind CSS)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm

# Set working directory
WORKDIR /app

# Copy entire repo
COPY . .

# Install Tailwind and build output.css
RUN npm install -D tailwindcss && \
    npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# Build Spring Boot JAR using Maven wrapper
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "target/scm2.0-0.0.1-SNAPSHOT.jar"]
