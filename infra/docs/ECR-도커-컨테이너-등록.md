> create boot file
- ./gradlew clean build
- ./gradlew :api:build
- ./gradlew :worker:build

> create docker file
- docker build -t api-service:latest .
- docker build -t worker-service:latest .

> push docker image to ecr
- aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin {AWS 정보}
- docker tag api-service:latest {ECR 정보}/api:latest
- docker push {ECR 정보}/api:latest
- docker tag worker-service:latest {ECR 정보}worker:latest
- docker push {ECR 정보}