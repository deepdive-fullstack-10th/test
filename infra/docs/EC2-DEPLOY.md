## EC2 배포 
- fargate의 특성을 사용할 수 없음
  - Rabbit MQ 때문에
- EC2 배포로 전환

---

### 배포 전 자바 설치

```bash
cat << 'EOF' > installCoreeto.sh
# 1. 먼저 필요한 패키지들 설치
sudo apt-get update
sudo apt-get install -y wget software-properties-common

# 2. Amazon Corretto 공개 키 및 apt 저장소 추가
wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
sudo add-apt-repository 'deb https://apt.corretto.aws stable main'

# 3. 시스템 패키지 업데이트
sudo apt-get update

# 4. Corretto 21 설치
sudo apt-get install -y java-21-amazon-corretto-jdk

# 5. 설치 확인
java -version
EOF
```

---

### 수동 배포 과정

1. aws cli에서 s3 버킷을 생성
    - aws s3 mb s3://cocomu-code-deployment
2. s3에 jar 업로드
    - aws s3 cp api/build/libs/api-0.0.1-SNAPSHOT.jar s3://cocomu-code-deployment/api.jar
3. SSM으로 private subnet ec2에 접속
    - aws ssm start-session --target {instance-id} --document-name AWS-StartInteractiveCommand --parameters command="sudo su -”
    - cd ~/../home/ubuntu/app 으로 이동, app 없으면 생성
4. env 파일 생성

    ```bash
    cat << EOF > .env
    SPRING_PROFILES_ACTIVE=dev
    RABBITMQ_HOST={private_ec2_ip}
    RABBITMQ_PORT=5672
    RABBITMQ_USERNAME=admin
    RABBITMQ_PASSWORD=admin123
    EOF
    ```

5. 서버에 배포하기 jar 실행
    - deploy.sh 생성
        ```bash
        cat << 'EOF' > deploy.sh
        #!/bin/bash
        
        APP_NAME="cocomu-api"
        DEPLOY_DIR="/home/ubuntu/app"
        JAR_NAME="api.jar"
        SERVER_PORT=8080  # 수동 배포는 8080 포트 고정
        
        # S3에서 jar 파일 다운로드
        aws s3 cp s3://cocomu-code-deployment/$JAR_NAME $DEPLOY_DIR/$JAR_NAME
        
        # 환경 변수 로드
        source .env
        
        # 기존 프로세스 종료 (있다면)
        kill -15 $(lsof -t -i:${SERVER_PORT}) 2>/dev/null || true
        sleep 5
        
        # 새 버전 실행
        echo "Starting application on port ${SERVER_PORT}"
        nohup java -jar \
            -Dserver.port=${SERVER_PORT} \
            -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
            -Drabbitmq.host=$RABBITMQ_HOST \
            -Drabbitmq.port=$RABBITMQ_PORT \
            -Drabbitmq.username=$RABBITMQ_USERNAME \
            -Drabbitmq.password=$RABBITMQ_PASSWORD \
            $DEPLOY_DIR/$JAR_NAME > $DEPLOY_DIR/app.log 2>&1 &
        
        echo "Application deployed. Check logs at $DEPLOY_DIR/app.log"
        EOF
        ```


    ```bash
    chmod +x deploy.sh
    ./deploy.sh
    ```

6. 실행 확인 및 resource 제거