FROM openjdk:23-jdk-slim

WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]