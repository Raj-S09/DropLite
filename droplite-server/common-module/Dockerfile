# Use Maven base image with OpenJDK 21 for building the project
FROM maven:3.9.9-eclipse-temurin-21 AS common-builder

# Set the working directory
WORKDIR /common

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Install the common module in the local repository
RUN mvn clean install