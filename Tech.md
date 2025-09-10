# Technical Requirements Document (Tech.md)

## AI RULE 설정

### 기술 요구사항 자동 기록 규칙
- 요구사항 중 기술적으로 다른 Agent에서 활용되어야 하는 내용은 이 Tech.md 파일에 자동으로 기록
- 기술적 세부사항과 아키텍처 관련 정보를 별도로 관리하여 개발 효율성 향상
- 새로운 기술 요구사항이 제시될 때마다 이 문서에 추가/업데이트

### 기존 요구사항 변화 최소화 원칙
- 작업 시 PRD.md와 Tech.md 두 파일을 항상 참조
- 기존 요구사항의 변화를 최소화하여 일관성 유지
- 새로운 요구사항 추가 시 기존 문서와의 중복 방지

---

## 기술 요구사항

### Java Thread Dump 분석 도구 아키텍처 (2024-01-01)

#### 기술 스택
- **Java Version**: JDK 21 (LTS)
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Gradle 7.0+
- **Template Engine**: Thymeleaf
- **Frontend**: Bootstrap 5, Font Awesome 6
- **Dependencies**: Apache Commons, Jackson, Logback

#### 핵심 컴포넌트
- **ThreadDumpParser**: Thread Dump 파싱 엔진
- **PatternAnalyzer**: 30개 패턴 분석 로직
- **ThreadDumpAnalyzerService**: 분석 서비스 오케스트레이션
- **ThreadDumpController**: 웹 컨트롤러

#### 데이터 모델
- **ThreadInfo**: 개별 스레드 정보 모델
- **ThreadDumpAnalysis**: 전체 분석 결과 모델
- **ProblemPattern**: 문제 패턴 모델

#### 패턴 분석 기술
- **정규표현식 기반**: 스택 트레이스 패턴 매칭
- **통계 분석**: 스레드 상태별 통계 계산
- **우선순위 알고리즘**: 심각도와 영향도 기반 순위 결정
- **신뢰도 점수**: 패턴 매칭 신뢰도 계산

#### 폐쇄망 환경 기술 요구사항
- **Fat JAR**: 모든 의존성을 포함한 실행 가능한 JAR
- **내장 리소스**: CSS, JS, 이미지 등 모든 정적 리소스 내장
- **오프라인 의존성**: 외부 API 호출 없는 완전 독립 실행
- **로컬 파일 시스템**: 로그 및 임시 파일은 로컬에 저장

#### 성능 요구사항
- **파싱 성능**: 대용량 Thread Dump 파일 처리 (최대 50MB)
- **메모리 효율성**: 스트리밍 방식의 파일 처리
- **응답 시간**: 분석 결과 3초 이내 제공
- **동시 처리**: 다중 사용자 동시 분석 지원

#### 보안 요구사항
- **파일 업로드 보안**: 파일 크기 및 형식 제한
- **입력 검증**: Thread Dump 내용 검증
- **로컬 실행**: 외부 네트워크 접근 차단

