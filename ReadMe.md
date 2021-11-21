Project - [WhyPie]
===

## Coming soon Deploy ..

- Project started when : 2021-10-02

## About

- Services
- Features
- Goals
    - Generate API Docs based on TC : https://062.notion.site/Spring-REST-Docs-API-19b0c3953ee54135afbd701be61413cf
- Diagrams
    - ![whypie_architecture](https://user-images.githubusercontent.com/14158670/137644112-f8dc6b20-c1bb-4fbc-98e9-9700ab9f5225.png)

- Issue tracking
- Git Flow : https://062.notion.site/Git-Branch-Flow-Chart-f244a100e55e47bd85b31f6d527912aa

## Development environment

|분류|상세|
| ---------- | :--------- |
|Language|Kotlin (OpenJDK 11)|
|Framework|Spring Boot 2.5.5, JPA|
|dependencies|Spring [REST Docs, HATEOAS, Batch, Security], QueryDSL, JWT|
|Build Tool|Gradle 7.2|
|DBMS|H2, MySQL 5.7|
|Repository|Redis|
|DevOps|EC2, ECR, Docker, Jenkins|

## Package structure

```
├─ application                       독립적으로 실행 가능한 어플리케이션 모듈 계층
│  ├─ back-opffice                       시스템 백오피스 관련 어플리케이션 모듈 계층
│  │  └─ project-manager-api
│  ├─ batch                              서비스 배치 관련 어플리케이션 모듈 계층
│  ├─ monitoring                         서비스 모니터링 어플리케이션 관련 모듈 계층
│  │  ├─ eureka
│  │  └─ spring-boot-admin
│  └─ service                            서비스 API 어플리케이션 모듈 계층
│     ├─ authorization-api
│     ├─ gateway-api
│     ├─ member-api
│     └─ whypie-api
├─ domain                            서비스 도메인 관련 비즈니스 로직을 다루는 중심 모듈 계층
│  ├─ member-service                     RDB 모듈과 Redis 모듈의 결합 트랜잭션을 다루는 모듈 계층
│  ├─ rds                                RDB 의존적인 Entity의 비즈니스 로직을 다루는 모듈 계층
│  │  ├─ common
│  │  ├─ member-core
│  │  └─ project-core
│  └─ redis                              Redis 의존적인 Entity의 비즈니스 로직을 다루는 모듈 계층
│     ├─ common
│     ├─ member-cache
│     └─ token-cache
└─ in-system                         실행 가능한 어플리케이션의 기능을 지원하기 위한 기능 단위의 내부 모듈 계층
   ├─ clinets                            외부 어플리케이션 통신 모듈 계층
   ├─ event-publisher                    내부 어플리케이션 Event-driven messaging 관련 모듈 계층
   └─ web-core                           Web 관련 어플리케이션 모듈 계층에서 사용 가능한 기능 단위 모듈 계층
      ├─ authorization-filter
      └─ exception-handler
```

## TODO

> Spring REST Docs + OAS3 + Git Book을 이용한 분산환경의 API Docs 통합 관리
> ```
> [#1] spring-rest-docs + MockMVC TC Result snippet를 이용한 OAS 산출
> [#2] restdocs-api-spec + git book을 이용한 Api Docs 생성
> ```

> CI/CD
> ```
> - CI를 위한 Jenkinsfile 작성
>   - [#1] SCM Clone
>   - [#2] Run TC and generated Test Coverage reports
>   - [#3] Gradle build
>   - [#4] Build lightweight container docker image by multi-stage
>   - [#5] Pusing image to ECR
>  - CD : with eks
> ```

> spring cloud config + private VCS를 이용한 설정 중앙화
>```
> spring-cloud-config server + github private(ssh) connection
>  - AS-IS : Native (file-based configurations)
>  - TO-BE : 
>    - GitHub Private SSH connection (remote repository configurations)
>    - encrypt configurations to spring-boot-jasypt  
>    - OR : spring-cloud-vault 고려
> ```

> 분산환경
> ```
> - sleuth + zipkin을 이용한 log transaction 처리
> - eureka & spring boot admin을 이용한 client discovery 적용
> ```

> jacoco를 이용한 test coverage 산출
