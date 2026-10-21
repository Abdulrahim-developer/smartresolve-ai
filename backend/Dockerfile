# Use a lightweight, production-ready Eclipse Temurin JDK 21 Alpine container
FROM eclipse-temurin:21-jre-alpine

# Set a secure, dedicated working directory inside the container system
WORKDIR /app

# Copy the freshly compiled executable binary straight into the execution container
COPY target/smartresolve-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the internal Docker network layer
EXPOSE 8080

# Execute the application on startup with optimal container performance parameters
ENTRYPOINT ["java", "-jar", "app.jar"]
