<div align=center>
<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=Trello&fontSize=75" />
</div>
<div align=center>
	<h3>📚 Backend Tech Stack 📚</h3>
</div>
<div align="center">
	<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Conda-Forge&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA-11DAFB?style=flat" />
  <img src="https://img.shields.io/badge/QueryDSL-4479A1?style=flat" />
  <br>
	<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=Spring&logoColor=white" />
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" />
  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=flat&logo=SpringSecurity&logoColor=white" />
  <br>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white" />
  <img src="https://img.shields.io/badge/AmazonS3-569A31?style=flat&logo=AmazonS3&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white" />
</div>

<div align=center>
	<h3>🛠️ Tools 🛠️</h3>
</div>
<div align="center">
	<img src="https://img.shields.io/badge/intellij-000000?style=flat&logo=intellijidea&logoColor=white" />
  	<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white" />
  	<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white" />
</div>

</p>
  <p align="center">
    <a href="https://https://github.com/Nameless1004/trello/graphs/contributors">
      <img alt="GitHub Contributors" src="https://img.shields.io/github/contributors/Nameless1004/trello" />
    </a>
    <br />
      <br />
  </p>
</div>

# 📕트렐로 프로젝트
## 프로젝트 설명

## 프로젝트 인원
* 김진근 (팀장)
* 강이원 (팀원)
* 정재호 (팀원)
* 유태이 (팀원)
## 담당 기능
* 김진근 (팀장)
	* 스프링 시큐리티 jwt 토큰
	* 멤버
 	* 워크스페이스
  	* 동시성 제어
* 강이원 (팀원)
	* 카드
 	* 댓글
 	* 첨부파일
  	* 레디스 캐싱 처리
* 정재호 (팀원)
	* 리스트
 	* 슬랙 알림
  	* 젠킨스 CI / CD
  	* 레디스 센티넬
* 유태이 (팀원)
	* 로그인 / 회원가입
 	* 보드
  	* 검색
  	* 쿼리 최적화
## 영상
<a href="https://www.youtube.com/watch?v=uaNDadpahXA" target='_blank'><img src="https://img.shields.io/badge/youtube-FF0000?style=flat&logo=youtube&logoColor=white" /></a>

<br>
<br>

# 백엔드
## ♻️ERD
![image](https://github.com/user-attachments/assets/e630dd3c-6174-4708-bd4e-9b633d3f40b3)
## 🗒️기능
<details>
<summary><h3>인증 인가</h3></summary>
<div markdown="1">

- jwt 액세스, 리프레쉬 토큰 발행
- jwt 액세스, 리프레쉬 토큰 재발행
- jwt 토큰 인증, 인가
- 회원가입 / 로그인 / 로그아웃
</div>
</details>

<details>
<summary><h3>동시성 제어</h3></summary>
<div markdown="1">

- 각 락을 설정하는 방법과 장단점
    - 낙관적 락
    - 비관적 락
    - 분산 락
- 낙관적 락을 선택한 이유
    - 일반적으로 카드의 상태가 동시에 업데이트되는 경우는 그리 많지 않다고 생각해, **낙관적 락**을 사용하는 것이 적합하다 판단함. 이는 성능 저하 없이 효율적으로 데이터 일관성을 유지하는 방식이며, 충돌이 발생했을 경우에도 재시도하는 구조로 간단하게 해결할 수 있습니다.
- 동시성 문제 해결 전후의 테스트 결과를 비교
    - 레포지토리의 `disable-concurrency-control.html` (동시성 제어 끈 후 테스트), `enable-concurrency-control.html` (동시성 제어 켠 후 테스트) 캡쳐 후 사용
</div>
</details>

<details>
<summary><h3>멤버 권한</h3></summary>
<div markdown="1">

- 워크스페이스의 멤버인지 / 멤버 역할이 `READ_ONLY`인데 `GET` 요청이 아닌 요청을 했을때를 확인하는 필터를 추가함
    - `RoleFilter.java`
    - uri에서 워크스페이스 id를 인식해서 자동으로 확인하도록 함
</div>
</details>

<details>
<summary><h3>캐싱</h3></summary>
<div markdown="1">

- 캐싱 후 조회 시간이 줄음 @casheable을 붙여줌
- 사용을 위해 `cacheManager` 를 RedisConfig에 @Been으로 추가해줌
![image](https://github.com/user-attachments/assets/e54b84c7-7813-4c4f-b501-4242f641e2ef)
- 캐싱 전 조회 시간이 길었음
![image](https://github.com/user-attachments/assets/0c4e8fff-7f1e-42c3-a152-c56d99e2da1a)
</div>
</details>

<details>
<summary><h3>쿼리 최적화</h3></summary>
<div markdown="1">

- 개선 대상 쿼리와 해당 쿼리를 선택한 이유
    - title 컬럼은 인덱스가 설정 되어 있지 않아 검색어가 포함된 제목을 찾을 때 전체 테이블 스캔을 해야 함 이로 인해 테이블의 레코드가 많을수록 성능이 저하 됨
    - title에 대해 인덱스를 설정하면, 부분 일치 검색에서 성능이 향상 됨
- 인덱스 설정 DDL 쿼리
```sql
CREATE INDEX idx_card_title ON cards(title);
```
- 쿼리 속도 비교 (Before, After)
```sql
EXPLAIN SELECT * FROM cards WHERE title LIKE '검색어%';
```
![image](https://github.com/user-attachments/assets/053a61a3-dd6b-41cb-a0ff-d6b077c2a57c)
![image](https://github.com/user-attachments/assets/4cb46cd8-68a5-41bd-a13d-0ee3cb633f38)
</div>
</details>
<details>

<summary><h3>CI/CD</h3></summary>
<div markdown="1">
	
- 도커로 젠킨스 구축
- 깃헙 웹 훅 url에 [`localhost:8888/`](http://localhost:8888/) 는 동작이 안돼서 `ngrok`을  사용해서 외부에서 내 로컬에 접속할 수 있도록 조치
![image](https://github.com/user-attachments/assets/ece74b5d-8f6f-49db-968b-58329460bb5c)
- 파이프라인
    - dev 브랜치에 푸쉬 (잘 동작되는지 확인을 위해서 dev 브랜치로 지정)
    - webhook으로 젠킨스에서 dev 브랜치에 푸쉬가 발생되면 파이프라인 실행
        - 깃허브 클론 받아오기
        - 빌드
        - Test 실행
        - 도커 이미지 빌드
        - 도커 허브에 이미지 푸쉬
        - ssh로 ec2 터미널 원격 접속
            - 도커 허브에서 최신 버전 pull
            - docker run으로 실행
- 결과
![image](https://github.com/user-attachments/assets/e457557c-a944-4bfc-bbeb-8b27d0b51de4)
![image](https://github.com/user-attachments/assets/1d5fe822-4daf-4cd6-898c-cc78ac9c699d)
</div>
</details>

# 트러블 슈팅
<details>
<summary><h2>캐시 조회 오류</h2></summary>
<div markdown="1">
	
### 배경
Redis와 Spring Boot를 사용하여 캐시를 설정 중, LocalDate 및 LocalDateTime 타입의 직렬화 문제로 인해 캐시 조회 시 오류가 발생했습니다.
### 발단
LocalDate와 LocalDateTime은 Jackson 라이브러리에서 기본적으로 직렬화가 지원되지 않아 Could not write JSON 오류가 발생했습니다. 이는 Java 8 날짜 API와 관련된 문제였습니다.
### 전개
문제 해결을 위해 Jackson 모듈 의존성(jackson-datatype-jsr310)을 추가하고, @JsonFormat 어노테이션을 사용해 날짜 직렬화를 시도했습니다. 또한, ModuleConfig 파일을 추가해ObjectMapper를 만들어 JavaTimeModule을 등록했으나 문제는 지속되었습니다.
### 위기
CacheManager에서 JSON 직렬화 처리가 잘못되었다는 점을 쉽게 발견하지 못했고, 원인을 찾기 어려운 상황이었습니다. GenericJackson2JsonRedisSerializer를 이미 사용 중이었기 때문에 문제가 캐시 설정과 관련이 있다고 생각하지 않았습니다.

**문제가 있던 원래 코드**
```java
@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // 캐시 만료 시간 설정
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig)
                .transactionAware() // 트랜잭션 지원
                .build();
    }
```
### 절정
문제의 원인은 CacheManager 설정에서 ObjectMapper가 제대로 직렬화 모듈을 인식하지 못한 것이었습니다. 결국 GenericJackson2JsonRedisSerializer에 ObjectMapper를 직접 전달하는 방식으로 문제를 해결할 수 있었습니다.
### 결말
문제 해결을 위해 CacheManager에서 GenericJackson2JsonRedisSerializer에 ObjectMapper를 전달하여 직렬화 문제를 해결하였고, 정상적으로 캐시 데이터를 직렬화 및 역직렬화할 수 있게 되었습니다.

**최종 해결된 코드**
```java
@Bean
public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    RedisSerializer<Object> redisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

    return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(cacheConfig)
            .transactionAware()
            .build();
}
```		
</div>
</details>
