WhyPie project package structure
===

## Development environment

|분류|상세|
| ---------- | :--------- |
|Language|Kotlin (OpenJDK 11)|
|Framework|Spring Boot 2.5.5, JPA|
|dependencies|Spring [REST Docs, HATEOAS, Batch, Security], QueryDSL, JWT,
|Build Tool|Gradle 7.2|
|DBMS|H2, MySQL 5.7|
|Repository|Redis|
|DevOps|EC2, ECR, Docker, Jenkins|

```
├─application                               독립적으로 실행 가능한 어플리케이션 모듈 계층
│  ├─back-opffice                               시스템 백오피스 관련 어플리케이션 모듈 계층
│  │  └─project-manager-api
│  ├─batch                                      서비스 배치 관련 어플리케이션 모듈 계층
│  ├─monitoring                                 서비스 모니터링 어플리케이션 관련 모듈 계층
│  │  └─eureka
│  │  └─spring-boot-admin
│  ├─service                                    서비스 API 어플리케이션 모듈 계층
│  │  └─authorization-api
│  │  └─gateway-api
│  │  └─member-api
│  │  └─whypie-api
├─domain                                    서비스 도메인 관련 비즈니스 로직을 다루는 코어 모듈 계층
│  ├─member-service
│  ├─rds                                        RDB 의존성을 가지는 Entity의 비즈니스 로직을 다루는 모듈 계층
│  │  ├─common
│  │  └─member
│  └─redis                                      Redis 의존성을 가지는 Entity의 비즈니스 로직을 다루는 모듈 계층
│      └─common
│      └─member
├─in-system-module                          실행가능한 어플리케이션의 기능을 서포트하기 위한 기능 단위 모듈 계층 
│  └─core-web                                   Web 관련 어플리케이션 모듈 계층에서 사용가능한 기능 단위 모듈 계층
│      └─authorization-filter
│      └─exception-handler
│  └─event-publisher                            내부 어플리케이션 event driven messaging 관련 모듈 계층
│  └─clinets                                    외부 어플리케이션 통신 모듈 계층
```