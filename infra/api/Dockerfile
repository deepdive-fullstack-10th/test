# 1. Java 기반 Docker 이미지 선택
FROM openjdk:21-jdk-slim

# 2. 빌드된 JAR 파일을 컨테이너로 복사=
ARG JAR_FILE=api/build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 3. 컨테이너에서 실행할 명령어
CMD ["java", "-jar", "/app.jar"]