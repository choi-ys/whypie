Project - [WhyPie]
===

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
│  │  └─ eureka
│  │  └─ spring-boot-admin
│  ├─ service                            서비스 API 어플리케이션 모듈 계층
│  │  └─ authorization-api
│  │  └─ gateway-api
│  │  └─ member-api
│  │  └─ whypie-api
├─ domain                            서비스 도메인 관련 비즈니스 로직을 다루는 중심 모듈 계층
│  ├─ member-service                     RDB 모듈과 Redis 모듈의 결합 트랜잭션을 다루는 모듈 계층
│  ├─ rds                                RDB 의존적인 Entity의 비즈니스 로직을 다루는 모듈 계층
│  │  ├─ common
│  │  └─ member-core
│  │  └─ project-core
│  └─ redis                              Redis 의존적인 Entity의 비즈니스 로직을 다루는 모듈 계층
│     └─ common
│     └─ member-cache
│     └─ token-cache
└─ in-system                         실행 가능한 어플리케이션의 기능을 지원하기 위한 기능 단위의 내부 모듈 계층
   ├─ clinets                            외부 어플리케이션 통신 모듈 계층
   ├─ event-publisher                    내부 어플리케이션 Event-driven messaging 관련 모듈 계층
   └─ web-core                           Web 관련 어플리케이션 모듈 계층에서 사용 가능한 기능 단위 모듈 계층
      └─ authorization-filter
      └─ exception-handler
```