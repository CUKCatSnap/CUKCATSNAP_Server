FROM amazoncorretto:17-al2023 AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY auth-shared auth-shared
COPY gateway gateway

RUN chmod +x gradlew
RUN ./gradlew :gateway:clean :gateway:build -x test --no-daemon

FROM amazoncorretto:17-al2023

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

WORKDIR /app

COPY --from=builder /app/gateway/build/libs/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]