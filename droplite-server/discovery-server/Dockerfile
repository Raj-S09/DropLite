# Use Maven base image with OpenJDK 21 for building the project
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY ../ .

# Build the project using Maven
RUN mvn clean package -DskipTests -pl  discovery-server -am

# Use OpenJDK 21 runtime for the final image
FROM openjdk:21-jdk-slim

# Set the working directory for the application
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/discovery-server/target/discovery-server.jar app.jar

# Expose the port the application will run on
EXPOSE 8761

# Run the application using Java 21
CMD ["java", "-jar", "app.jar"]