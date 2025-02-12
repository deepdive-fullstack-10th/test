### api-service-dev Task Definition
```json
{
  "memory": "1024",
  "cpu": "512",
  "containerDefinitions": [
    {
      "portMappings": [
        {
          "hostPort": 8080,
          "containerPort": 8080,
          "protocol": "tcp",
          "appProtocol": "http"
        }
      ],
      "essential": true,
      "name": "application-api-service-dev",
      "cpu": 512,
      "memory": 1024,
      "image": "597088034205.dkr.ecr.ap-northeast-2.amazonaws.com/cocomu/api-service-dev:0",
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "cocomu-application-staging",
          "awslogs-region": "ap-northeast-2",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "environment": [
        {"name": "SPRING_PROFILES_ACTIVE", "value": "dev"}
      ]
    }
  ],
  "networkMode": "awsvpc",
  "family": "application-api-service-dev",
  "runtimePlatform": {
    "cpuArchitecture": "X86_64",
    "operatingSystemFamily": "UBUNTU"
  },
  "requiresCompatibilities": ["FARGATE"],
  "taskRoleArn": "arn:aws:iam::597088034205:role/ecs-task-rule",
  "executionRoleArn": "arn:aws:iam::597088034205:role/ecs-task-rule",
  "tags": [
    {
      "key": "env",
      "value": "dev"
    }
  ]
}
```

### worker-service-dev task definition
```json
{
  "memory": "1024",
  "cpu": "512",
  "containerDefinitions": [
    {
      "portMappings": [
        {
          "hostPort": 8081,
          "containerPort": 8081,
          "protocol": "tcp",
          "appProtocol": "http"
        }
      ],
      "essential": true,
      "name": "application-worker-service-dev",
      "cpu": 512,
      "memory": 1024,
      "image": "597088034205.dkr.ecr.ap-northeast-2.amazonaws.com/cocomu/worker-service-dev:0",
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "cocomu-application-staging",
          "awslogs-region": "ap-northeast-2",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "environment": [
        {"name": "SPRING_PROFILES_ACTIVE", "value": "dev"}
      ]
    }
  ],
  "networkMode": "awsvpc",
  "family": "application-worker-service-dev",
  "runtimePlatform": {
    "cpuArchitecture": "X86_64",
    "operatingSystemFamily": "UBUNTU"
  },
  "requiresCompatibilities": ["FARGATE"],
  "taskRoleArn": "arn:aws:iam::597088034205:role/ecs-task-rule",
  "executionRoleArn": "arn:aws:iam::597088034205:role/ecs-task-rule",
  "tags": [
    {
      "key": "env",
      "value": "dev"
    }
  ]
}
```
