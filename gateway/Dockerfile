FROM amazoncorretto:17-amazonlinux AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY auth-shared auth-shared
COPY gateway gateway

RUN chmod +x gradlew
RUN ./gradlew :gateway:clean :gateway:build -x test --no-daemon

FROM amazoncorretto:17-amazonlinux

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/gateway/build/libs/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]