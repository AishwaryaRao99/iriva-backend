
# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration first
COPY pom.xml .

# Retry Maven dependency resolution if Maven Central temporarily fails
RUN set -eu; \
    retry() { \
        attempt=1; \
        until "$@"; do \
            if [ "$attempt" -ge 3 ]; then return 1; fi; \
            echo "Maven request failed. Retrying in $((attempt * 30)) seconds..."; \
            sleep $((attempt * 30)); \
            attempt=$((attempt + 1)); \
        done; \
    }; \
    retry mvn -B -ntp dependency:go-offline

# Copy application source code
COPY . .

# Build the application
RUN set -eu; \
    retry() { \
        attempt=1; \
        until "$@"; do \
            if [ "$attempt" -ge 3 ]; then return 1; fi; \
            echo "Maven build failed. Retrying in $((attempt * 30)) seconds..."; \
            sleep $((attempt * 30)); \
            attempt=$((attempt + 1)); \
        done; \
    }; \
    retry mvn -B -ntp clean package -DskipTests

# Stage 2: Run the application using a slim Java runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
