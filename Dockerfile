# Use lightweight Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Add the built JAR into the image
COPY target/Mybackendintellij-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]
