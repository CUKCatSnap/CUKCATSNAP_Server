<<<<<<< Updated upstream
FROM amazoncorretto:17-al2023 AS builder
=======
FROM eclipse-temurin:17-jdk AS builder
>>>>>>> Stashed changes

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY auth-shared auth-shared
COPY gateway gateway

RUN chmod +x gradlew
RUN ./gradlew :gateway:clean :gateway:build -x test --no-daemon

<<<<<<< Updated upstream
FROM amazoncorretto:17-al2023

RUN groupadd -r appgroup && useradd -r -g appgroup appuser
=======

FROM eclipse-temurin:17-jre

RUN groupadd -g 10001 appgroup \
 && useradd  -u 10001 -g appgroup -s /usr/sbin/nologin -m appuser
>>>>>>> Stashed changes

WORKDIR /app

COPY --from=builder /app/gateway/build/libs/*.jar /app/app.jar

RUN chown appuser:appgroup /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
