# 🚀 Performance Monitoring Starter

> Spring Boot 애플리케이션의 성능 병목을 자동으로 탐지하는 경량 모니터링 라이브러리

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)

## 📋 목차

- [특징](#-특징)
- [시작하기](#-시작하기)
- [사용법](#-사용법)
- [모니터링 결과 확인](#-모니터링-결과-확인)
- [주요 기능](#-주요-기능)
- [예제](#-예제)
- [성능 고려사항](#-성능-고려사항)
- [문제 해결](#-문제-해결)
- [기여하기](#-기여하기)
- [라이선스](#-라이선스)

## ✨ 특징

- 🎯 **간단한 사용**: `@PerformanceMonitoring` 어노테이션만으로 즉시 사용 가능
- 📊 **N+1 쿼리 자동 탐지**: 쿼리 패턴 분석으로 N+1 문제 자동 감지
- 🌳 **메서드 호출 트리**: 계층별 메서드 호출 관계를 트리 구조로 시각화
- ⚡ **순수 실행시간**: 자식 메서드 시간을 제외한 순수 실행시간 계산
- 🔄 **비동기 저장 지원**: 메인 로직에 영향 없는 비동기 메트릭 저장
- 🧵 **Thread-Safe**: ThreadLocal 기반 멀티스레드 환경 완벽 지원
- 📈 **실시간 모니터링**: 웹 대시보드에서 실시간 성능 확인

## 🚀 시작하기

### 요구사항

- Java 17 이상
- Spring Boot 3.2.0 이상
- Maven 또는 Gradle

### 설치 방법

이 라이브러리는 Maven Central에 배포되어 있지 않습니다. 로컬에서 빌드하여 사용하세요.

#### Step 1: 저장소 클론
```bash
git clone https://github.com/suhyun9764/performance-monitoring-library.git
cd performance-starter
```

#### Step 2: 로컬 빌드 및 설치

**Maven 사용 시:**
```bash
./mvnw clean install
```

**Gradle 사용 시:**
```bash
./gradlew clean build publishToMavenLocal
```

#### Step 3: 사용할 프로젝트에 의존성 추가

**Maven (`pom.xml`):**
```xml
<dependency>
    <groupId>com.suhyun</groupId>
    <artifactId>performance-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle (`build.gradle`):**
```gradle
repositories {
    mavenLocal()  // 로컬 Maven 저장소 추가
}

dependencies {
    implementation 'com.suhyun:performance-starter:1.0.0'
}
```

#### Step 4: Spring Boot 프로젝트 재시작

의존성이 추가되면 자동으로 설정됩니다. 별도의 설정 없이 바로 사용 가능합니다!

---

## 📖 사용법

### 1. 기본 사용

메서드나 클래스에 `@PerformanceMonitoring` 어노테이션을 추가하세요.
```java
@RestController
@RequestMapping("/api")
public class UserController {
    
    @PerformanceMonitoring  // 메서드 레벨
    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```
```java
@Service
@PerformanceMonitoring  // 클래스 레벨 (모든 public 메서드에 적용)
public class UserService {
    
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow();
    }
}
```

### 2. N+1 쿼리 감지

`Repoistory`를 호출하는 계층에서 `queryMonitoring = true` 옵션을 사용하세요

`Repository`에서 직접 사용할 경우 `N+1` 탐지 확률이 낮아질 가능성이 있습니다.
```java
@Service
@PerformanceMonitoring(queryMonitoring = true)  // N+1 감지 활성화
public class UserService {
    @Transactional(readOnly = true)
    public List<UserResponse> findAllWithPosts() {
        return userRepository.findAll().stream()
            .map(this::toResponseWithPosts)  // N+1 발생 가능 지점
            .toList();
    }
}
```

## 📊 모니터링 결과 확인

### 웹 대시보드

애플리케이션 실행 후 브라우저에서 접속:
```
http://localhost:8080/performance
```

### 결과 예시
```json
{
  "traceId": "a1b2c3d4",
  "totalTime": 185,
  "metrics": [
    {
      "layer": "CONTROLLER",
      "method": "UserController.getUser()",
      "executionTime": 185,
      "selfExecutionTime": 28,
      "depth": 1,
      "calledBy": null
    },
    {
      "layer": "SERVICE",
      "method": "UserService.findById()",
      "executionTime": 85,
      "selfExecutionTime": 17,
      "depth": 2,
      "calledBy": "exec-100",
      "queryInfo": {
        "totalQueryCount": 67,
        "hasNPlusOne": true,
        "nPlusOneIssues": [
          {
            "pattern": "select post_id, content from posts where user_id=?",
            "count": 48,
            "ratio": 71.6
          }
        ]
      }
    }
  ]
}
```

## 🎯 주요 기능

### 1. 메서드 호출 관계 추적

Stack 기반으로 메서드 호출 관계를 추적하고 부모-자식 관계를 명확히 파악합니다.
```
Controller.getUser()           28ms (순수)
├─ Service.findById()          85ms (순수: 17ms)
│  ├─ Repository.findById()    68ms
│  └─ Mapper.toResponse()      0ms
└─ ResponseBuilder.build()     72ms
```

### 2. N+1 쿼리 자동 탐지

동일한 쿼리 패턴이 반복 실행되는 경우 자동으로 감지합니다.

**감지 기준:**
- 동일 패턴 쿼리가 전체의 30% 이상
- 반복 횟수 5회 이상

### 3. 순수 실행시간 계산

메서드의 전체 실행시간에서 자식 메서드 실행시간을 제외한 순수 실행시간을 계산합니다.
```java
// 순수 시간 = 전체 시간 - 자식들 시간
selfExecutionTime = executionTime - sum(children.executionTime)
```

### 4. Thread-Safe 메트릭 수집

ThreadLocal 기반으로 멀티스레드 환경에서도 요청별 데이터를 안전하게 격리합니다.

## 💡 예제

### Controller → Service → Repository 추적
```java
@RestController
@PerformanceMonitoring
public class PostController {
    
    @GetMapping("/posts")
    public List<PostResponse> getPosts() {
        return postService.findAllWithComments();
    }
}

@Service
public class PostService {
    
    @PerformanceMonitoring(queryMonitoring = true)
    @Transactional(readOnly = true)
    public List<PostResponse> findAllWithComments() {
        List<Post> posts = postRepository.findAll();
        
        return posts.stream()
            .map(post -> {
                List<Comment> comments = commentRepository.findByPostId(post.getId());
                return new PostResponse(post, comments);
            })
            .toList();
    }
}
```

**감지 결과:**
```
⚠️ N+1 Query Detected!
패턴: select * from comments where post_id=?
반복: 48회 (71.6%)
```

## ⚙️ 성능 고려사항

### 메모리 사용

- 요청당 메트릭 데이터: 약 1-2KB
- ThreadLocal 사용으로 Thread Pool 환경에서도 안전
- 자동 `remove()` 처리로 메모리 누수 방지

### 성능 영향

- AOP 오버헤드: 메서드당 < 1ms
- 비동기 저장으로 메인 로직 영향 최소화
- 프로덕션 환경: Profile 설정으로 선택적 활성화 권장
```yaml
# application-prod.yml
performance:
  monitoring:
    enabled: false  # 프로덕션 환경에서는 비활성화 권장
```

**Made  by [신수현](https://github.com/suhyun9764)**
