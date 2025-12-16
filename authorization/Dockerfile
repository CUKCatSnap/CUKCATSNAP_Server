FROM  amazoncorretto:17-alpine AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY auth-shared auth-shared
COPY authorization authorization

RUN chmod +x gradlew
RUN ./gradlew :authorization:clean :authorization:build -x test --no-daemon

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=builder /app/authorization/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]