# 1단계: 빌드
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# 의존성을 먼저 받아 둔다. 소스만 바뀌면 이 단계는 다시 실행되지 않는다.
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon -q > /dev/null

COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 관리자 권한이 아닌 전용 사용자로 실행한다
RUN useradd --system --create-home appuser \
    && mkdir -p /app/media /app/logs \
    && chown -R appuser /app
USER appuser

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
