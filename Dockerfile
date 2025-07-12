# ========== Stage 1: Build the JAR ==========
FROM eclipse-temurin:21 AS maven-builder

WORKDIR /app

# Copy project files
COPY . .

# Give permission to Maven wrapper if needed
RUN chmod +x mvnw

# Generate latest Tailwind CSS
RUN npm install -D tailwindcss postcss autoprefixer && \
    npx tailwindcss -i ./src/main/resources/static/input.css -o ./src/main/resources/static/output.css --minify

# Clean and package the Spring Boot app (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests


# ========== Stage 2: Run the App ==========
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=maven-builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
