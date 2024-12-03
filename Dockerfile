# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src
COPY confs/filters ./src/main/resources/
COPY confs/keys ./src/main/resources/
COPY confs/telegram ./src/main/resources/

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /opt
RUN mkdir -p /opt/nest/
RUN mkdir -p /opt/nest/store/
RUN mkdir -p /opt/nest/logs/

# Copy only the built JAR file from the builder stage
COPY --from=builder /app/target/NewNest.jar /opt/NewNest.jar

# Command to run the application
CMD ["java", "-jar", "NewNest.jar"]
