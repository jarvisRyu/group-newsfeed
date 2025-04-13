
# 📰Newsfeed 프로젝트

## 1️⃣ 프로젝트 개요 📋

Newsfeed는 SNS에서 자주 사용하는 게시글, 댓글, 친구 기능 등을 직접 구현해보며 **Spring Boot 기반 백엔드 시스템의 전체 흐름(인증, CRUD, 관계 설정 등)을 실습하고 협업 개발 경험을 쌓는 데 중점을 둔 팀 프로젝트**입니다.

- **진행 기간**: 2025.04.07 ~ 2025.04.14
- **참여 인원**: 4명
- **주요 기술 스택**: Java 17, Spring Boot, JPA, MySQL, Lombok, Validation, GitHub, Notion 등

🌈 ERD

유저, 게시글, 친구, 댓글

<img width="948" alt="erd" src="https://github.com/user-attachments/assets/9a9ad56c-9e71-4eca-a622-024e40089639" />

🌈 SA

역할 분담, 팀 규칙, api 등등 프로젝트의 전반적인 내용을 확인할 수 있습니다. 

https://www.notion.so/teamsparta/SA-1ce2dc3ef51480789104f3869db0b480?pvs=4

🌈 API 명세서

링크

https://www.notion.so/teamsparta/SA-1ce2dc3ef51480789104f3869db0b480?pvs=4


🛠 기술 스택

| Category | Tech |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Database | MySQL |
| ORM | Spring Data JPA |
| Build Tool | Gradle |
| API Docs | Notion |
| Version Control | Git & GitHub |
| IDE | IntelliJ IDEA |

🌈 디렉토리 구조

 groupnewsfeed
    ├── GroupNewsfeedApplication.java
    ├── comment
    │   ├── controller
    │   │   └── CommentController.java
    │   ├── dto
    │   │   ├── CommentAllResponseDto.java
    │   │   ├── CommentRequestDto.java
    │   │   ├── CommentResponseDto.java
    │   │   ├── EditCommentRequestDto.java
    │   │   └── EditedResponseDto.java
    │   ├── entity
    │   │   └── Comment.java
    │   ├── repository
    │   │   └── CommentRepository.java
    │   └── service
    │       └── CommentService.java
    ├── common
    │   ├── advice
    │   │   ├── CustomException.java
    │   │   ├── ErrorCode.java
    │   │   ├── ErrorResponse.java
    │   │   └── GlobalExceptionHandler.java
    │   ├── constant
    │   │   └── Const.java
    │   ├── entity
    │   │   └── BaseEntity.java
    │   └── filter
    │       ├── LoginFilter.java
    │       └── WebConfig.java
    ├── config
    │   └── PasswordEncoder.java
    ├── friend
    │   ├── controller
    │   │   └── FriendController.java
    │   ├── dto
    │   │   ├── CreateFriendRequestDto.java
    │   │   ├── CreateFriendResponseDto.java
    │   │   └── FriendResponseDto.java
    │   ├── entity
    │   │   └── Friend.java
    │   ├── repository
    │   │   └── FriendRepository.java
    │   └── service
    │       └── FriendService.java
    ├── post
    │   ├── controller
    │   │   └── PostController.java
    │   ├── dto
    │   │   ├── request
    │   │   │   └── CreateAndUpdadePostRequestDto.java
    │   │   └── response
    │   │       ├── CreatePostResponseDto.java
    │   │       ├── GetPostWhitCommentDto.java
    │   │       └── UpdatePostResponseDto.java
    │   ├── entity
    │   │   └── Post.java
    │   ├── repository
    │   │   └── PostRepository.java
    │   └── service
    │       └── PostService.java
    └── user
        ├── controller
        │   ├── LoginController.java
        │   └── UserController.java
        ├── dto
        │   ├── FindUserIdResponseDto.java
        │   ├── SignOutRequestDto.java
        │   ├── SignUpRequestDto.java
        │   ├── SignUpResponseDto.java
        │   ├── UpdateIntroductionRequestDto.java
        │   ├── UpdateUserRequestDto.java
        │   ├── UserLoginRequestDto.java
        │   ├── UserLoginResponseDto.java
        │   └── UserResponseDto.java
        ├── entity
        │   └── User.java
        ├── repository
        │   └── UserRepository.java
        └── service
            └── UserService.java



***

## 2️⃣ 실행 방법 🔨 

1. 필요한 환경 구성하기

- Java Jdk: 17버전
- IDE: IntelliJ IDEA

2. 프로젝트 클론하기

[[https://github.com/hosung77/schedule](https://github.com/hosung77/upgradedSchedule)](https://github.com/jarvisRyu/group-newsfeed)

3. Main에서 실행

***
## 3️⃣ Git 전략 ⚡️ 

- gitflow 방식 적용

브랜치는 main, dev, feature/로 나누어 관리하였으며, 기능 개발은 feature 브랜치, 긴급 수정은 fix 브랜치, 리펙토링은 fix 브랜치에서 개발 진행하였고
기능 단위로 dev 브랜치에 Pull Request를 통해 병합하였다. 전체 기능 개발이 완료되면, dev 브랜치를 main에 병합하여 최종 코드를 반영하였다.

## 4️⃣ 브랜치 개발 순서 🔨

main -> dev -> feature/fix/refactor -> dev -> main

## 5️⃣ 주요 기능 ⚙️

### ✅ **회원 가입**

사용자 이름, 이메일, 비밀번호를 이용하여 가입이 가능합니다. 이메일은 이메일 형식으로 가입해야 하며 비밀번호는 특수문자, 영문자, 숫자를 포함하여 8자 이상으로 가입하여야 합니다.

### ✅ **로그인**

가입시에 입력한 이메일과 비밀번호를 가지고 로그인이 가능합니다.

### ✅ **로그아웃**

로그아웃을 통해 세션에 저장된 나의 정보를 지울 수 있습니다.

### ✅ **회원 정보 수정**

회원이 정보를 수정할 수 있습니다.

### ✅ **회원 탈퇴**

회원 탈퇴를 통해 db에 저장된 회원 정보를 지울 수 있습니다.

### ✅ **회원 조회**

회원 조회를 통해 회원의 정보를 확인할 수 있습니다.

### ✅ **본인 조회**

본인의 정보를 조회할 수 있습니다.

### ✅ **모든 회원 조회**

가입된 모든 회원을 확인할 수 있습니다.

### ✅ **게시글 생성**

내용을 입력하여 게시글을 생성할 수 있습니다.

### ✅ **게시글 조회하기**

모든 게시글을 조회할 수 있습니다.

### ✅ **특정 게시글 불러오기**

특정 게시글을 조회할 수 있습니다.

### ✅ **게시글 수정**

본인의 일정을 원하는 정보만 수정이 가능하다.

### ✅ **게시글 삭제**

자신의 특정 게시물을 삭제할 수 있습니다.

### ✅ **댓글 작성**

모든 게시물에 자유롭게 댓글을 작성할 수 있습니다.

### ✅ **댓글 목록 조회**

특정 게시글의 댓글 목록을 조회할 수 있습니다.

### ✅ **댓글 수정**

댓글 작성자가 본인의 댓글을 수정할 수 있습니다.

### ✅ **댓글 삭제**

댓글 작성자 및 게시글 주인이 댓글을 삭제할 수 있습니다.

### ✅ **Validation**

@Valid를 활용하여 Validation 기능을 적용하여 이메일 형식, 비밀번호 형식 등에 대한 제한사항을 추가해주었습니다.

### ✅ **비밀번호 암호화**

회원가입, 일정 생성, 댓글 작성시 입력된 비밀번호는 db에 저장시 암호화를 적용한 후 저장하였습니다.

### ✅ **자기소개 작성**

프로필에 자기소개를 작성할 수 있습니다.

### ✅ **친구 요청**

원하는 상대방에게 친구 요청을 보낼 수 있습니다.

### ✅ **친구 요청 수락 및 거절**

친구 요청을 받은 사람은 요청을 수락 및 거절할 수 있습니다.

### ✅ **친구 목록 조회**

자신의 친구 목록을 조회할 수 있습니다.

### ✅ **친구 단건 조회**

자신의 친구 중 특정 친구를 조회할 수 있습니다.

### ✅ **요청 받은 친구 목록 조회**

요청 받은 친구의 목록을 조회할 수 있습니다.

### ✅ **친구 삭제**

친구 관계를 삭제할 수 있습니다.



