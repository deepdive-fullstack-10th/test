## Change History

---

### 6. CodeExecution 학습

#### ✅ [6-2] STOMP SSE 적용

- 서버 STOMP 설정
- Front STOMP 구독
- 코드 실행 요청 후 STOMP로 응답받기

#### ✅ [6-1] CodeExecution 기본 셋팅

- IDE UI 작성
- 코드 실행 후 Server로 부터 응답받기
- Anonymous Filter 적용

### 5. Security 학습

#### ✅ [5-2] open api Security 적용

- open Api 중 사용자 정보의 비교가 필요할 때를 처리
- userList를 통해 본인 정보인지 확인
- Filter Chain에 UsernamePasswordAuthenticationFilter 전에 검증하는 필터를 추가
  - 토큰이 있다면 사용자 id를 전달
  - 토큰이 없다면 사용자 id를 임의(0L, DB 충돌 X)로 전달

#### ✅ [5-1] api Security 적용

- access Token 정보로 사용자 정보 조회
- refresh Token 은 HttpOnly 적용
- Filter chain에 OncePerFilter 체이닝
  - token 검증이 필요한 요청에 대해 검증

### 4. Oauth 학습

- Server Side Oauth vs Client Side Oauth를 이해하기 위한 학습
- H2 DB, JPA, Security를 활용해서 학습을 진행한다.
- 깃헙 Oauth를 연동한다.

#### ✅ [4-6] jwt 발급

- 사용자 ID를 기준으로 Jwt token을 발급한다.
- accessToken과 refreshToken을 발급
- 사용자 id로 로그인한 사용자 조회

#### ✅ [4-5] 구글 OAuth 추가

- Google OAuth 정보를 추가한다.

#### ✅ [4-4] 카카오 OAuth 추가

- Kakao OAuth 정보를 추가한다.

#### ✅ [4-3] Refactoring

- 재사용성을 고려해서 리팩토링을 진행한다.

#### ✅ [4-2] Client Side Oauth 연동

- static 폴더의 login.html로 oauth 로그인 페이지를 만든다.
  - 깃헙 로그인을 생성한다.
  - front에서 oauth code를 받는다.
  - server에서 로그인해본다.
- Client Side 방식으로 으로 Oauth 로그인이 되는지 확인한다.
  - OAuth Code 정보로 로그인 요청
  - 회원가입 완료되었다는 응답만 추가

#### ✅ [4-1] User 엔티티 생성

- Spring Data JPA와 H2 DB를 사용해서 서버를 실행한다.
  - 설정이 제대로 됐는지 확인
- Security 설정을 통해 H2 DB로 접속이 되는지 확인한다.
- User Entity를 작성하고 H2 DB에 제대로 생성이 되었는지 확인한다.

### 3. Yorkie 학습

#### ✅ [3-2] Yorkie 추가 정보 정리하기

- [Yorkie 기술 정리](./yorkie/NOTE.md)에 내가 이해한 내용을 정리
- [Yorkie docs](https://yorkie.dev/docs/js-sdk)에 상세한 내용
- 3-1 이해 후 3-2를 이해해야된다.

#### ✅ [3-1] Yorkie Document 따라하기

- 기존 Web IDE 개발 학습 시 문제점
    - AI를 활용한 개발을 했음.
    - AI가 최신 Yorkie 정보를 가지고 있지 않음.
- Yorkie Docs 기반 최신 문서를 기준으로 학습 가능
- [Yorkie - Getting Started](https://yorkie.dev/docs/getting-started/with-js-sdk) 을 이해해보자.

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