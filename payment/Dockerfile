FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle wrapper 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .

# payment 빌드를 위한 settings.gradle 생성 (payment만 포함)
RUN printf "include 'payment'\n" > settings.gradle

# 의존성 다운로드를 위한 빌드 파일 복사
COPY payment/build.gradle payment/build.gradle

# Gradle wrapper 실행 권한 부여
RUN chmod +x gradlew

# 의존성 다운로드
RUN ./gradlew :payment:dependencies --no-daemon || true

# 전체 소스 코드 복사
COPY payment payment

# 빌드 실행
RUN ./gradlew :payment:clean :payment:build -x test --no-daemon


FROM eclipse-temurin:17-jre

RUN groupadd -g 10001 appgroup \
 && useradd -u 10001 -g appgroup -s /usr/sbin/nologin -m appuser

WORKDIR /app

COPY --from=builder /app/payment/build/libs/*.jar /app/app.jar

RUN chown appuser:appgroup /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
