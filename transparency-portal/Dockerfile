
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Configure alternative Maven Central mirror
COPY settings.xml /tmp/settings.xml

# Copy Maven configuration
COPY pom.xml .

# Download dependencies using Google mirror
RUN mvn -s /tmp/settings.xml -B -ntp dependency:go-offline

# Copy application source
COPY src ./src

# Build application
RUN mvn -s /tmp/settings.xml -B -ntp clean package -DskipTests

# Stage 2: Run application
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
