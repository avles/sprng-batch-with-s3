# Start with a base image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the executable jar file and any necessary configuration files
COPY target/batch-microservice-0.0.1.jar /app
COPY src/main/resources/application.yml /app

# Expose any necessary ports
EXPOSE 8080

# Set the default command to run the application
CMD ["java", "-jar", "batch-microservice-0.0.1.jar"]
