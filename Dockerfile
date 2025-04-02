# Use the official Maven image to build the application
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a minimal JRE for running the app
FROM eclipse-temurin:17-jre

# Set the working directory
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
