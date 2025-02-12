# 1. Java 기반 Docker 이미지 선택
FROM openjdk:21-jdk-slim


# 2. 필요한 패키지 설치
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    software-properties-common

# gVisor 공식 GPG 키 추가
RUN wget -q -O - https://gvisor.dev/archive.key | apt-key add -

# gVisor 저장소 추가
RUN add-apt-repository "deb https://storage.googleapis.com/gvisor-release stable main"

# gVisor 설치
RUN apt-get update && apt-get install -y runsc

# 3. 빌드된 JAR 파일을 컨테이너로 복사
ARG JAR_FILE=worker/build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 4. 컨테이너에서 실행할 명령어
CMD ["java", "-jar", "/app.jar"]