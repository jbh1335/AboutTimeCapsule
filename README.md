# Readme

# 💊프로젝트 소개

![Frame 4](https://github.com/JinseonHeo/assignment/assets/31607944/c9b70f35-c276-47ac-bef2-080bf6c57a8d)
---

## 1. 프로젝트 일정

### 🗓️2023.04.10(월) ~ 2023.05.19(금)🗓️

## 2. 기획 배경

### ⌛타임캡슐이란?

- 타임캡슐은 현재의 시간과 메시지, 사진 등을 포함하여 미래의 어느 시점에 열어 볼 수 있도록 묻어두는 것을 말합니다.
- 이는 과거의 추억을 전하는 것뿐만 아니라, 미래의 사람들에게 메시지를 전하거나 특정한 의미를 기념하기 위해 사용될 수 있습니다.

### 💡타임캡슐을 소환한 이유?

- 타임캡슐은 특정 시간에 대한 추억을 보존합니다.
- 우리의 삶은 변화하고 성장하며, 시간이 흐름에 따라 과거의 경험들은 희미해지고 잊혀질 수 있습니다.
- 타임캡슐은 그 순간의 감정과 기억을 소중히 간직하며, 미래의 세대들과 공유할 수 있는 기회를 제공합니다.

### 📱왜 디지털 타임캡슐일까?

- 영구적 보관
- 보다 쉬운 접근 및 공유
- AR을 통해 현실과 디지털 세계 연결

## 3. 기대 효과

### ✨추억 회상

- 같은 장소에 방문했을 때 이전의 추억을 생생하게 되돌아볼 수 있어요.

### 💑추억 공유

- 나의 소중한 추억들을 친구들과 함께 나눌 수 있어요.

### 🤩독특한 경험

- AR을 통해 실제 타임캡슐을 열어보는 듯한 생동감을 느낄 수 있어요.

# 💊개발 환경

---

## 1. 개발 환경

### 🐸IDE

- Android Studio: `Flamingo 2022.2.1`
- Intellij: `2022.3.1`

### 🐹프론트엔드

- Kotlin: `1.8.0`
- JAVA: `11.0.17`

### 🐷백엔드

- JAVA: `11.0.18`
- MySQL: `8.0.32`
- SpringBoot: `2.7.11`

### 🐳인프라

- Docker: `23.0.5`
- Jenkins: `2.60.3`

## 2. 협업 환경

- 이슈 관리: JIRA
- 형상 관리: GitLab
- 커뮤니케이션: Notion, Mattermost, Webex
- 디자인: Figma
- UCC: Movavi
- CI/CD: Jenkins

## 3. 사용 라이브러리

### 🐶프론트엔드

- 카카오 로그인 SDK `2.13.0`
- 네이버 로그인 SDK `5.5.0`
- retrofit `2.9.0`
- coroutine `1.3.9`
- lifecycle-viewModel `2.4.0`
- glide `4.15.1`
- jetpack navigation `2.5.3`
- FireBase-bom `32.0.0`
- FireBase-messaging `23.1.2`
- AR Core `1.37.0`

### 🐥백엔드

- SpringBoot
- Spring Web
- Lombok
- Spring Data JPA
- Spring Data Redis
- Spring Cloud (Gateway, Eureka)
- Spring Security
- Oauth2
- Redis
- Firebase
- MySQL
- SSL
- Swagger

## 4. 프로젝트 구조

```bash
📦BE
 ┗ 📂capsule/member/oauth-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂timecapsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂oauthservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂api
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂redis
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂db
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂jwt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂povider
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📂firebase
 ┃ ┃ ┃ ┃ ┣ 📜application-oauth.yml
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┣ 📜favicon.ico
 ┃ ┃ ┃ ┃ ┗ 📜keystore.p12
 ┃ ┣ 📜.gitignore
 ┃ ┣ 📜build.gradle
 ┃ ┣ 📜Dockerfile
 ┃ ┣ 📜gradlew
 ┃ ┣ 📜gradlew.bat
 ┃ ┣ 📜Jenkinsfile
 ┃ ┗ 📜settings.gradle
 ┃
 ┃
 ┗ 📂apigateway-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂timecapsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂apigatewayservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ApigatewayServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜keystore.p12
 ┃ ┣ 📜.gitignore
 ┃ ┣ 📜build.gradle
 ┃ ┣ 📜Dockerfile
 ┃ ┣ 📜gradlew
 ┃ ┣ 📜gradlew.bat
 ┃ ┣ 📜Jenkinsfile
 ┃ ┗ 📜settings.gradle
 ┃
 ┃
 ┗ 📂discovery-service
 ┃ ┣ 📂src
 ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┗ 📂timecapsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂discoveryservice
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜DiscoveryServiceApplication.java
 ┃ ┃ ┃ ┗ 📂resources
 ┃ ┃ ┃ ┃ ┣ 📜application.yml
 ┃ ┃ ┃ ┃ ┗ 📜keystore.p12
 ┃ ┣ 📜.gitignore
 ┃ ┣ 📜build.gradle
 ┃ ┣ 📜Dockerfile
 ┃ ┣ 📜gradlew
 ┃ ┣ 📜gradlew.bat
 ┃ ┣ 📜Jenkinsfile
 ┃ ┗ 📜settings.gradle
 ┃
 ┃
 ┃
📦FE
 ┣ 📂AboutTimeCapsule
 ┃ ┣ 📂app
 ┃ ┃ ┣ 📂release
 ┃ ┃ ┃ ┗ 📜output-metadata.json
 ┃ ┃ ┣ 📂src
 ┃ ┃ ┃ ┣ 📂androidTest
 ┃ ┃ ┃ ┃ ┗ 📂java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂aboutcapsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂android
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ExampleInstrumentedTest.kt
 ┃ ┃ ┃ ┣ 📂main
 ┃ ┃ ┃ ┃ ┣ 📂java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂aboutcapsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂android
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂data
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂capsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂memory
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mypage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂oauth
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂factory
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂views
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂ar
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂capsule
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂chat
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂login
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mainpage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂map
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂mypage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂notification
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MainActivity.kt
 ┃ ┃ ┃ ┃ ┣ 📂res
 ┃ ┃ ┃ ┃ ┃ ┣ 📂color
 ┃ ┃ ┃ ┃ ┃ ┣ 📂drawable
 ┃ ┃ ┃ ┃ ┃ ┣ 📂drawable-v24
 ┃ ┃ ┃ ┃ ┃ ┣ 📂font
 ┃ ┃ ┃ ┃ ┃ ┣ 📂layout
 ┃ ┃ ┃ ┃ ┃ ┣ 📂menu
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-anydpi-v26
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-hdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-mdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xxhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mipmap-xxxhdpi
 ┃ ┃ ┃ ┃ ┃ ┣ 📂navigation
 ┃ ┃ ┃ ┃ ┃ ┣ 📂values
 ┃ ┃ ┃ ┃ ┃ ┣ 📂values-night
 ┃ ┃ ┃ ┃ ┃ ┣ 📂values-v23
 ┃ ┃ ┃ ┃ ┃ ┗ 📂xml
 ┃ ┃ ┃ ┃ ┗ 📜AndroidManifest.xml
 ┃ ┣ 📜.gitignore
 ┃ ┣ 📜build.gradle
 ┃ ┣ 📜gradle.properties
 ┃ ┣ 📜gradlew
 ┃ ┣ 📜gradlew.bat
 ┃ ┗ 📜settings.gradle
```

# 💊기능

---

### 1. 로그인

<img title="" src="exec/readme_assets/login.gif" alt="19" width="132">

### 2. 캡슐 등록 → 개인/그룹

<img title="" src="exec/readme_assets/캡슐등록.gif" alt="19" width="132">

### 3. 추억 등록

<img title="" src="exec/readme_assets/추억%20생성.gif" alt="19" width="133">

### 4. 나의 캡슐/친구의 캡슐/나의 방문 기록

<img title="" src="exec/readme_assets/나의캡슐조회.gif" alt="19" width="136"> <img title="" src="exec/readme_assets/친구캡슐조회.gif" alt="19" width="136">

### 5. 지도에서 캡슐 조회

<img title="" src="exec/readme_assets/지도에서 잠긴캡슐조회.gif" alt="19" width="138">

### 6. AR로 조회

<img title="" src="exec/readme_assets/AR.gif" alt="19" width="137">

### 7. 캡슐 열람

<img title="" src="exec/readme_assets/지도%20그룹캡슐조회.gif" alt="19" width="137">

### 8. 마이페이지

<img title="" src="exec/readme_assets/프로필 조회.gif" alt="19" width="137">

### 9. 친구 관리

<img title="" src="exec/readme_assets/모든 친구 조회.gif" alt="19" width="137">

# 💊기술 소개

---

## 1. MSA

- MSA 구조를 적용하여 서버의 부하를 방지하고 에러 발생시 전체 서비스로 확장 방지합니다.
  
  | MSA | MicroService Architecture으 약자로, 각각을 마이크로하게 나눈 독립적인 서비스를 연결한 구조로 이러한 특성 덕분에 시스템 전체의 중단 없이 필요한 부분만 업데이트/배포가 가능 |
  | --- | ------------------------------------------------------------------------------------------------------------ |

## 2. Spring Security + JWT

- Spring Security를 베이스로 JWT를 사용하여 인증과 인가 구현하여 보안을 강화합니다.
  
  | Spring Security | Spring 기반의 애플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크                  |
  | --------------- | ----------------------------------------------------------------------- |
  | JWT             | JSON Web Token의 약자로, Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token |

## 3. MVVM 패턴

- MVVM 패턴을 적용하여 단위 별로 의존성을 줄이고 View와 Model 의 독립적인 개발 방식을 적용합니다.
  
  | MVVM 패턴 | 페이지를 Model, View, ViewModel 단위로 분리한 패턴으로, MVC 패턴에서 Controller를 빼고 ViewModel을 추가한 패턴 |
  | ------- | ----------------------------------------------------------------------------------- |

## 4. ARCore

- 캡슐 찾는 과정에 AR을 적용하여 현실 세계에서 디치털 세계의 캡슐을 생생하게 경험할 수 있도록 합니다.
  
  | ARCore | 증강 현실 애플리케이션을 구축할 수 있도록 Google에서 개발한 소프트웨어 개발 키트 |
  | ------ | ------------------------------------------------ |

## 5. FCM

- 나의 캡슐이 오픈되거나 친구와 공유된 캡슐을 생성되었을 때 알림을 통해 서비스를 편하게 이용할 수 있도록 합니다. 또한 나의 추억에 댓글이 달리거나 친구 요청과 관련된 상황을 알림을 통해 실시간으로 확인할 수 있습니다.
  
  | FCM | Firebase Cloud Messaging으 약자로, 무료로 메시지를 안정적으로 전송할 수 있는 크로스 플랫폼 메시징 솔루션 |
  | --- | ---------------------------------------------------------------------- |

## 6. Redis

- 로그인 시 사용되는 Refresh Token을 저장하여 빠른 액세스 속도로 로그인 시 병목 현상을 방지합니다.

- 알림 토큰 및 진행 상황을 Redis에 저장하여 뛰어난 속도로 데이터에 접근할 수 있습니다.
  
  | Redis | Remote Dictionary Server의 약자로, "키-값" 구조의 비정형 데이터를 저장하고 관리하기 위한 오픈 소스 기반의 비관계형 데이터베이스 관리 시스템 |
  | ----- | ------------------------------------------------------------------------------------------- |

# 💊산출물

---

## 1. 아키텍처

![image](./exec/readme_assets/어바웃타임캡슐_아키텍처.png)

## 2. ERD

![image](./exec/readme_assets/ERD.png)

## 3. 기능 명세서

![image](./exec/readme_assets/기능명세서.png)

## 4. API 명세서

![image](./exec/readme_assets/API%20명세서.png)

# 💊팀 소개

---

| 👑김아린                                                                                         | 🐶강정훈                                                                                          | 🐸장별하                                                                                          | 🐹장유범                                                                                                                                          | 🐻허진선                                                                                         |
| --------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| <img src="https://avatars.githubusercontent.com/u/67595512?v=4" width = "100" height = "100"> | <img src="https://avatars.githubusercontent.com/u/109566855?v=4" width = "100" height = "100"> | <img src="https://avatars.githubusercontent.com/u/119313897?v=4" width = "100" height = "100"> | <img src="https://avatars.githubusercontent.com/u/96399409?s=400&u=31b8717e8e28885edadfd52403e851455a6bb8bc&v=4" width = "100" height = "100"> | <img src="https://avatars.githubusercontent.com/u/31607944?v=4" width = "100" height = "100"> |
| <p>**팀장/백엔드**</p>Infra <br>아키텍쳐 <br> AR                                                       | <p>**프론트**</p>마이페이지 구현<br>캡슐페이지 구현                                                             | <p>**백엔드**</p> 캡슐 API 구현<br> 멤버 API 구현<br> 알림 서비스                                              | <p>**프론트**</p>지도페이지 구현<br>캡슐페이지 구현                                                                                                             | <p>**백엔드**</p> Auth API 구현                                                                    |
