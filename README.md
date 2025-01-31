## Change History

---

### 2. Web IDE 개발

#### ✅ [2-5] 사용자 별 Tab 적용

- Tab UI 작성
- IDE 내 사용자 별 Tab 정보를 적용
    - 임시로 Mock Data를 활용
    - 추후 API를 통해 수정할 수 있도록 모듈화
- 우선은 탭 하나만 적용

#### ✅ [2-4] 다양한 언어 적용

- IDE 별 js, python, java 언어를 적용
- 디렉토리 구조 조정
- js 모듈화, 객체지향적으로 재설계

#### ✅ [2-3] IDE 동시 편집 적용

- Monaco IDE를 활용해 IDE 생성
- Client 별로 하나의 IDE 세션에 연결 됐는지 확인
    - Room을 통해 각 Room 별로 세션의 연결을 확인
    - 같은 세션에 Client들의 연결 정보를 확인

#### ✅ [2-2] Yorkie Client Connection

- Yorkie Server와 커넥션을 확인


#### ✅ [2-1] Yorkie 서버 구성

- Docker-compose를 통해 서버 구성
    - mongodb, yorkie 설정을 간편하게 한 번에 처리 가능
- env 파일 설정
    - 환경 변수 정보를 노출하지 않음.
    - 예시 파일을 위해 example 생성

``` bash
# bash
docker-compose up -d
```

### 1. 초기 Spring Boot 환경설정

#### ✅ [1-1] 초기 환경 설정