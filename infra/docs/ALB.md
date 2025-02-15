## ALB 라우팅 및 Route 53 

### ALB (Application Load Balancer)

https://www.notion.so/24jihwan/18f281b0b4b680a6a4d4dd45778ae4b3?pvs=4#19b281b0b4b68067bff9dd013157dbcc

여기 정리했음.

### Route 53 

- ALB를 생성 후 rabbit.cocomu.co.kr 은 rabbitmq manage console로 라우팅
- api.cocomu.co.kr 은 spring boot api로 라우팅 되도록 할 수 있다.

1. route 53에서 alb 생성 후 만들어진 dns 주소를 alias로 설정
2. A record로 각 서브도메인 설정

### 라우팅

route 53 작업이 끝났으면 ALB에서 각 도메인 주소를 라우팅 해줘야한다.

> alb 리스너 설정 

- 80 -> 443
- 443 -> 규칙 추가, 호스트 헤더
- 각 규칙의 호스트 헤더로 sub domain 작성
- 각 호스트 헤더의 target group을 작성