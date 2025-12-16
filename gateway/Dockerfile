FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY auth-shared auth-shared
COPY gateway gateway

RUN echo "include 'gateway'" > settings.gradle && \
    echo "include 'auth-shared'" >> settings.gradle

RUN chmod +x gradlew
RUN ./gradlew :gateway:clean :gateway:build -x test --no-daemon

FROM eclipse-temurin:17-jre

RUN groupadd -g 10001 appgroup \
 && useradd  -u 10001 -g appgroup -s /usr/sbin/nologin -m appuser

WORKDIR /app

COPY --from=builder /app/gateway/build/libs/*.jar /app/app.jar

RUN chown appuser:appgroup /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
