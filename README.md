# 북적 Book적 Book카페 - 도서관리 시스템 (Backend)

프론트엔드 React 애플리케이션과 연동하여 데이터의 영속성을 제공하는 Spring Boot 기반의 백엔드 시스템입니다.

도서 데이터의 등록, 목록 조회, 검색, 수정, 삭제(CRUD) 요청을 처리하며, 프론트엔드에서 생성한 Base64 형식의 AI 표지 이미지 데이터를 데이터베이스에 안정적으로 저장하고 제공합니다.

본 프로젝트는 KT AIVLE School AI 트랙 미니프로젝트 5차 과제인  
**도서관리시스템 개발 (백엔드 통합)** 을 기반으로 진행했습니다.

---

# 1. 프로젝트 개요

## 1.1 프로젝트명

**북적 Book적 Book카페 - 도서관리 시스템 (Backend)**

---

## 1.2 프로젝트 목적

기존 `json-server`를 대체하여, 확장 가능하고 안정적인 Spring Boot 기반의 RESTful API 서버를 구축합니다. 프론트엔드 애플리케이션과 연동하여 도서 데이터를 RDBMS에 안전하게 관리하는 것을 목표로 합니다.

---

## 1.3 프로젝트 배경

프론트엔드 실습 환경에서 사용하던 `json-server`는 학습 목적으로는 훌륭하지만, 실제 서비스 환경에서는 데이터의 무결성, 보안, 동시성 처리 등에 한계가 있습니다.

이에 따라 실무에서 널리 사용되는 Java와 Spring Boot 프레임워크를 활용하여 백엔드 서버를 직접 구축하고, H2 데이터베이스(MySQL 모드)와 JPA를 연동하여 실제 서비스와 유사한 데이터 영속성 관리 환경을 구성했습니다.

---

## 1.4 주요 기능

- **RESTful API 제공**: 프론트엔드 통신을 위한 표준 API 엔드포인트 제공
- **도서 데이터 영속화**: H2 데이터베이스(파일 기반)를 활용한 영구적인 데이터 저장
- **CORS 설정**: 프론트엔드(React) 환경에서의 API 접근 허용
- **도서 CRUD 처리**:
  - 도서 목록 전체 조회 (`findAll`)
  - 도서 제목 기준 검색 (`findByTitleContaining`)
  - 도서 등록 (`create`)
  - 도서 수정 (`update` - 조회수, 좋아요, 정보 수정)
  - 도서 삭제 (`deleteById`)

---

## 1.5 최종 산출물

| 산출물 | 설명 |
|---|---|
| 소스코드 | Spring Boot 기반 프로젝트 코드 |
| README.md | 백엔드 프로젝트 개요, 실행 방법, API 명세 |
| application.yaml | 데이터베이스 및 서버 환경 설정 파일 |
| data/bookdb.mv.db | 파일 기반 H2 데이터베이스 파일 |

---

# 2. 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x |
| ORM | Spring Data JPA, Hibernate |
| Database | H2 Database (File mode, MySQL 호환 모드) |
| 빌드 도구 | Gradle |
| 기타 라이브러리 | Lombok, Spring Web, Spring Boot DevTools |

---

## 2.1 기술 선택 이유

| 기술 | 선택 이유 |
|---|---|
| Spring Boot | 설정의 간소화와 내장 톰캣 제공으로 빠른 REST API 서버 구축 가능 |
| Spring Data JPA | 객체 지향적인 데이터 접근과 반복적인 CRUD 쿼리 작성 최소화 |
| H2 Database | 가볍고 설정이 쉬우며, 파일 모드를 통해 로컬 환경에서도 데이터 영속성 유지 가능 |
| Lombok | Getter, Setter, 생성자 등의 반복 코드를 어노테이션으로 간소화하여 가독성 향상 |

---

# 3. 시스템 구조

```text
Spring Boot Backend
├─ Controller (REST API 엔드포인트 처리)
│  └─ Service (비즈니스 로직 및 트랜잭션 관리)
│     └─ Repository (Spring Data JPA, DB 접근)
│        └─ H2 Database (데이터 영속화)
```

---

## 3.1 핵심 구조

- `Controller`: 클라이언트(React)의 HTTP 요청을 받고, 응답을 JSON 형태로 반환합니다.
- `Service`: 컨트롤러와 리포지토리 사이에서 비즈니스 로직을 수행하며 트랜잭션을 관리합니다.
- `Repository`: `JpaRepository`를 상속받아 데이터베이스의 기본적인 CRUD 메서드를 제공받습니다.
- `Domain (Entity)`: 데이터베이스 테이블과 매핑되는 자바 객체입니다 (`Book.java`).

---

# 4. 실행 방법 가이드

## 4.1 개발 환경 요구사항

- **Java 17** 이상 설치 필요
- IDE (IntelliJ IDEA, Eclipse 등) 권장

---

## 4.2 프로젝트 클론 및 빌드

```bash
# 프로젝트 루트 폴더에서 진행
./gradlew build
```

---

## 4.3 백엔드 서버 실행

```bash
# IDE에서 메인 클래스 실행 또는 터미널 명령
./gradlew bootRun
```

기본 접속 주소:

```text
http://localhost:8080
```

---

## 4.4 H2 데이터베이스 콘솔 접속

브라우저를 통해 내장된 H2 콘솔에 접속하여 직접 데이터를 확인할 수 있습니다.

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/bookdb;MODE=MySQL;DATABASE_TO_UPPER=false`
- **User Name**: `sa`
- **Password**: `1234`

---

## 4.5 실행 체크리스트

- [ ] Java 17 버전 확인
- [ ] `./gradlew bootRun` 정상 실행 (포트 8080)
- [ ] 프론트엔드 환경 파일(예: `.env`)의 API_URL을 `http://localhost:8080`으로 변경 (json-server 포트 3000 사용 중지)
- [ ] H2 콘솔 정상 접속 확인

---

# 5. API 명세 요약

```text
Base URL: http://localhost:8080
Resource: /api/books (예시, 실제 구현된 Controller 매핑에 따라 다를 수 있음)
```

| 기능 | Method | Endpoint | 설명 |
|---|---|---|---|
| 도서 목록 전체 조회 | GET | `/api/books` | DB에 저장된 모든 도서 목록 반환 |
| 도서 검색 (제목) | GET | `/api/books/search?keyword={keyword}` | 제목에 키워드가 포함된 도서 검색 |
| 도서 등록 | POST | `/api/books` | 새로운 도서 정보(표지 이미지 포함) DB 저장 |
| 도서 정보 수정 | PUT/PATCH | `/api/books/{id}` | 기존 도서의 내용 및 상태(조회수, 좋아요 등) 수정 |
| 도서 삭제 | DELETE | `/api/books/{id}` | 특정 ID의 도서 데이터베이스에서 영구 삭제 |

*(주의: 실제 작성된 `BookController.java`의 RequestMapping 경로에 맞게 프론트엔드 API 호출 주소를 맞춰야 합니다.)*

---

# 6. 데이터베이스 스키마 (Entity 구조)

`Book` 엔티티 구조 (`books` 테이블)

| 필드명 | 타입 | 제약 조건 | 설명 |
|---|---|---|---|
| `book_id` | BIGINT | PK, AUTO_INCREMENT | 도서 고유 ID |
| `title` | VARCHAR(255) | NOT NULL | 도서 제목 |
| `author` | VARCHAR(255) | NOT NULL | 작가 이름 |
| `content` | TEXT | | 도서 내용 |
| `cover_image_url` | VARCHAR(MAX) | | AI 생성 표지 이미지 (Base64 등) |
| `likes` | INT | DEFAULT 0 | 좋아요 수 |
| `views` | INT | DEFAULT 0 | 조회수 |
| `created_at` | DATETIME | NOT NULL, UPDATABLE=FALSE | 등록일시 |
| `updated_at` | DATETIME | NOT NULL | 수정일시 |

---

# 7. 주요 트러블슈팅 및 고려사항

## 7.1 대용량 Base64 이미지 처리

**이슈**: 프론트엔드에서 생성된 Base64 이미지를 DB에 저장할 때 문자열 길이가 너무 길어 `VARCHAR(255)` 기본 길이를 초과하는 문제 발생.
**해결**: `Book` 엔티티의 `coverImageUrl` 필드에 `@Column(columnDefinition = "TEXT")` 또는 `LONGTEXT` 매핑을 적용하여 대용량 문자열을 저장할 수 있도록 처리. (현재 H2-MySQL 모드에서 적절한 타입 사용)

## 7.2 CORS (Cross-Origin Resource Sharing) 이슈

**이슈**: React 개발 서버(포트 5173)에서 Spring Boot 서버(포트 8080)로 API 요청 시 브라우저 정책에 의해 차단됨.
**해결**: Spring Boot 설정 클래스(`WebMvcConfigurer` 구현) 또는 Controller에 `@CrossOrigin` 어노테이션을 적용하여 프론트엔드 도메인의 접근을 허용.

---

# 8. 백엔드 개발 과정

## 8.1 개발 환경 구성 및 설정

- Spring Initializr를 통한 프로젝트 생성
- `application.yaml` 설정 (포트, H2 DB 설정, JPA 설정)

## 8.2 Entity 및 Repository 구현

- 데이터베이스 테이블과 매핑되는 `Book` 엔티티 클래스 생성
- JPA 자동 생성 시간 관리를 위한 `@CreationTimestamp`, `@UpdateTimestamp` 적용
- JpaRepository를 상속받는 `BookRepository` 인터페이스 생성 및 커스텀 검색 메서드(`findByTitleContaining`) 추가

## 8.3 Service 레이어 구현

- `BookServiece` 클래스를 생성하여 트랜잭션(`@Transactional`) 관리
- Controller에서 호출할 비즈니스 로직 메서드(create, update, delete, findAll, searchByKeyword) 구현

## 8.4 Controller 구현 및 API 연동 준비

- RESTful API 통신을 위한 엔드포인트 설계 및 매핑
- 프론트엔드와 정상적인 통신을 확인하고 JSON 데이터의 직렬화/역직렬화 구조 점검