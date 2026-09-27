# --- Build stage -----------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
# Warm the dependency cache in its own layer so code-only changes skip re-downloading
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests=false

# --- Runtime stage -----------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /build/target/task-api.jar app.jar
USER app
EXPOSE 8080
ENV PORT=8080
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:${PORT}/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
