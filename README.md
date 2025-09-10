# Java Thread Dump Analyzer

JDK21, Spring Boot, Gradle 기반의 Java Thread Dump 분석 도구입니다. 폐쇄망 환경에서 사용할 수 있도록 모든 의존성을 포함하여 구성되었습니다.

## 주요 기능

### 🔍 자동 문제점 감지
- **30가지 이상의 패턴 분석**: 데이터베이스, 메모리, 네트워크, 동기화, 스레드 풀 등 다양한 영역의 문제점을 자동으로 감지
- **TOP 3 우선순위 제시**: 발견된 문제점을 심각도와 영향도에 따라 우선순위를 매겨 상위 3개를 제시
- **신뢰도 점수**: 각 문제점에 대한 신뢰도를 백분율로 표시

### 📊 상세 분석
- **스레드 상태 통계**: RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED 상태별 스레드 수
- **개별 스레드 정보**: 각 스레드의 상세 정보와 스택 트레이스 제공
- **락 정보 분석**: 락 경합 및 데드락 감지

### 🌐 웹 기반 UI
- **직관적인 인터페이스**: 파일 업로드 또는 직접 입력을 통한 Thread Dump 분석
- **반응형 디자인**: 모바일 및 데스크톱 환경 모두 지원
- **실시간 검색**: 스레드 이름으로 실시간 검색 기능

## 지원하는 패턴

### 데이터베이스 관련
- `DATABASE_DEADLOCK`: 데이터베이스 트랜잭션 간 데드락
- `DATABASE_TIMEOUT`: 데이터베이스 쿼리 타임아웃
- `DATABASE_POOL_EXHAUSTED`: 데이터베이스 연결 풀 고갈

### 메모리 관련
- `OUT_OF_MEMORY`: JVM 힙 메모리 부족
- `MEMORY_LEAK`: 메모리 누수 의심
- `GC_PRESSURE`: 가비지 컬렉션 압박

### 네트워크 관련
- `NETWORK_TIMEOUT`: 네트워크 연결 타임아웃
- `HTTP_CLIENT_BLOCKED`: HTTP 클라이언트 블록
- `SOCKET_IO`: 소켓 I/O 블록

### 동기화 관련
- `DEADLOCK`: Java 레벨 데드락
- `LOCK_CONTENTION`: 락 경합
- `WAIT_NOTIFY`: wait/notify 관련 문제

### 스레드 풀 관련
- `THREAD_POOL_EXHAUSTED`: 스레드 풀 고갈
- `THREAD_STARVATION`: 스레드 기아 상태

### 프레임워크 관련
- `SPRING_BEAN_CREATION`: Spring Bean 생성 블록
- `HIBERNATE_SESSION`: Hibernate 세션 문제
- `JPA_QUERY`: JPA 쿼리 문제

### 캐시 관련
- `CACHE_MISS`: 캐시 미스
- `CACHE_EVICTION`: 캐시 제거

### 메시징 관련
- `MESSAGE_QUEUE`: 메시지 큐 블록
- `MESSAGE_CONSUMER`: 메시지 컨슈머 문제

### 기타 시스템 패턴
- `FILE_IO_BLOCKED`: 파일 I/O 블록
- `LOGGING_BLOCKED`: 로깅 블록
- `SECURITY_MANAGER`: 보안 관리자 문제
- `SERIALIZATION`: 직렬화/역직렬화 블록
- `REFLECTION`: 리플렉션 과사용
- `JIT_COMPILATION`: JIT 컴파일
- `JMX_MONITORING`: JMX 모니터링
- `SYSTEM_PROPERTIES`: 시스템 프로퍼티 접근

## 설치 및 실행

### 요구사항
- JDK 21 이상
- Gradle 7.0 이상 (선택사항)

### 빌드 및 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd ArchiThreads

# Gradle을 사용한 빌드
./gradlew build

# JAR 파일 실행
java -jar build/libs/thread-dump-analyzer.jar

# 또는 Gradle을 통한 직접 실행
./gradlew bootRun
```

### 접속
웹 브라우저에서 `http://localhost:38089`으로 접속

## 사용법

### 1. Thread Dump 파일 업로드
- 홈페이지에서 "파일 선택" 버튼을 클릭하여 Thread Dump 파일을 선택
- 지원 형식: `.txt`, `.log`, `.dump`

### 2. 직접 입력
- Thread Dump 내용을 텍스트 영역에 직접 붙여넣기

### 3. 분석 결과 확인
- **기본 분석**: TOP 3 문제점과 통계 정보 확인
- **상세 확인**: 개별 스레드의 상세 정보와 스택 트레이스 확인

## 폐쇄망 환경 지원

이 도구는 폐쇄망 환경에서 사용할 수 있도록 설계되었습니다:

- **모든 의존성 포함**: 외부 라이브러리와 리소스가 모두 JAR 파일에 포함
- **오프라인 동작**: 분석 과정에서 외부 API 호출 없음
- **정적 리소스 포함**: CSS, JavaScript 등 모든 웹 리소스가 내장

## 기술 스택

- **Backend**: Spring Boot 3.2.0, Java 21
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Build Tool**: Gradle
- **Dependencies**: 
  - Apache Commons Lang3
  - Apache Commons IO
  - Jackson (JSON 처리)
  - Logback (로깅)

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/archithreads/analyzer/
│   │   ├── controller/          # 웹 컨트롤러
│   │   ├── model/              # 데이터 모델
│   │   ├── service/            # 비즈니스 로직
│   │   └── ThreadDumpAnalyzerApplication.java
│   └── resources/
│       ├── static/             # 정적 리소스
│       ├── templates/          # Thymeleaf 템플릿
│       └── application.yml     # 설정 파일
└── test/                       # 테스트 코드
```

## API 엔드포인트

- `GET /`: 메인 페이지
- `POST /analyze`: Thread Dump 분석
- `GET /patterns`: 패턴 정보 페이지
- `GET /detailed`: 상세 스레드 정보

## 로그

애플리케이션 로그는 `logs/thread-dump-analyzer.log` 파일에 저장됩니다.

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 기여

버그 리포트나 기능 제안은 GitHub Issues를 통해 제출해 주세요.

## 지원

문제가 발생하거나 질문이 있으시면 GitHub Issues를 통해 문의해 주세요.
