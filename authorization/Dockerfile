FROM eclipse-temurin:17-jdk AS builder


WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY auth-shared auth-shared
COPY authorization authorization

RUN chmod +x gradlew
RUN ./gradlew :authorization:clean :authorization:build -x test --no-daemon


FROM eclipse-temurin:17-jre

RUN groupadd -g 10001 appgroup \
 && useradd -u 10001 -g appgroup -s /usr/sbin/nologin -m appuser

WORKDIR /app

COPY --from=builder /app/authorization/build/libs/*.jar /app/app.jar

RUN chown appuser:appgroup /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
