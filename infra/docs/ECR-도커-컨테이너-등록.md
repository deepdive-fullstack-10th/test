> create boot file
- ./gradlew clean
- ./gradlew :api:build
- ./gradlew :worker:build

> create docker file
- docker build -t api-service-dev:latest -f api/Dockerfile .
- docker build -t worker-service-dev:latest -f worker/Dockerfile .

> push docker image to ecr
- aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin {AWS 정보}
- docker tag api-service-dev:latest {ECR 정보}/api-service-dev:latest
- docker push {ECR 정보}/api-service-dev:latest
- docker tag worker-service-dev:latest {ECR 정보}/worker-service-dev:latest
- docker push {ECR 정보}/worker-service-dev:latest