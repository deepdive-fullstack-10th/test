개발 환경이므로 세부 정책을 정하지 않고 전체 권한을 허용

### ECR CI/CD 정책
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
              "logs:CreateLogStream",    // CloudWatch 로그 스트림 생성
              "ecr:*",                   // ECR 모든 권한 (이미지 push/pull 등)
              "logs:PutLogEvents"        // CloudWatch에 로그 쓰기
            ],
            "Resource": "*"
        }
    ]
}
```

> ### 정책 설명
> - logs:CreateLogStream
>  - CloudWatch 로그 스트림 생성
> - ecr:*
>   - ECR 모든 권한 (이미지 push/pull 등)
> - logs:PutLogEvents
>   - CloudWatch에 로그 쓰기

---

### ECS CI/CD 정책
```json{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "application-autoscaling:Describe*",
                "application-autoscaling:PutScalingPolicy",
                "application-autoscaling:DeleteScalingPolicy",
                "application-autoscaling:RegisterScalableTarget",
                "cloudwatch:DescribeAlarms",
                "cloudwatch:PutMetricAlarm",
                "ecs:List*",
                "ecs:ExecuteCommand",
                "ecs:Describe*",
                "ecs:UpdateService",
                "iam:PassRole",
                "iam:AttachRolePolicy",
                "iam:CreateRole",
                "iam:GetPolicy",
                "iam:GetPolicyVersion",
                "iam:GetRole",
                "iam:ListAttachedRolePolicies",
                "iam:ListRoles",
                "iam:ListGroups",
                "iam:ListUsers"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
}
```

> 정책 설명
> - application-autoscaling:Describe*
>   - 오토스케일링 설정 조회
> - application-autoscaling:PutScalingPolicy
>   - 스케일링 정책 생성
> - application-autoscaling:DeleteScalingPolicy
>   - 스케일링 정책 삭제
> - application-autoscaling:RegisterScalableTarget
>   - 스케일링 대상 등록
> - cloudwatch:DescribeAlarms
>   - CloudWatch 알람 조회
> - cloudwatch:PutMetricAlarm
>   - CloudWatch 알람 생성
> - ecs:List*
>   - ECS 리소스 목록 조회
> - ecs:ExecuteCommand
>   - ECS 컨테이너 명령 실행
> - ecs:Describe*
>   - ECS 리소스 상세 조회
> - ecs:UpdateService
>   - ECS 서비스 업데이트
> - iam:PassRole
>   - IAM 역할 전달
> - 그 외, IAM 관련 역할 정책