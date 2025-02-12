### ECS Role 추가

작성되지 않은 내용은 Default

> ECS TASK가 AWS 서비스에 접근하기 위한 권한 설정

- 엔터티 유형: AWS 서비스
- 사용 사례: Elastic Container Service - TASK
- 권한
  - CloudWatchFullAccess
  - AmazonEC2ContainerRegistryFullAccess
- 역할 이름: ecs-task-rule

---

> Jenkins에서 CI/CD 작업을 위한 권한 설정
> ECS 

- 엔터티 유형: AWS 계정
- 권한
  - cicd-ecs
  - cicd-ecr
  - CloudWatchFullAccess
  - AmazonECS_FullAccess
  - AmazonECSTaskExecutionRolePolicy
- 역할 이름: ecs-cicd-deploy
