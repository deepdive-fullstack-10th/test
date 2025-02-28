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
        {"name": "SPRING_PROFILES_ACTIVE", "value": "dev"},
        {"name": "RABBITMQ_HOST", "value": "10.0.136.103"},
        {"name": "RABBITMQ_PORT", "value": "5672"},
        {"name": "RABBITMQ_USERNAME", "value": "admin"},
        {"name": "RABBITMQ_PASSWORD", "value": "admin123!@#"}
      ]
    }
  ],
  "networkMode": "awsvpc",
  "family": "application-api-service-dev",
  "runtimePlatform": {
    "cpuArchitecture": "X86_64",
    "operatingSystemFamily": "LINUX"
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