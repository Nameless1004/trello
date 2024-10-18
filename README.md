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
## 담당 기능
## 영상
<a href="" target='_blank'><img src="https://img.shields.io/badge/youtube-FF0000?style=flat&logo=youtube&logoColor=white" /></a>

<br>
<br>

# 백엔드
## 📜API 명세서
## ♻️ERD
## 🗒️기능
### 인증/인가
* jwt 액세스, 리프레쉬 토큰 발행
* jwt 액세스, 리프레쉬 토큰 재발행
* jwt 토큰 인증, 인가
* 회원가입 / 로그인 / 로그아웃

# 성능 개선

# 트러블 슈팅
### 1. **배경**

- Redis와 Spring Boot를 사용하여 캐시를 설정 중, `LocalDate` 및 `LocalDateTime` 타입의 직렬화 문제로 인해 캐시 조회 시 오류가 발생했습니다.

### 2. **발단**

- `LocalDate`와 `LocalDateTime`은 Jackson 라이브러리에서 기본적으로 직렬화가 지원되지 않아 `Could not write JSON` 오류가 발생했습니다. 이는 Java 8 날짜 API와 관련된 문제였습니다.

### 3. **전개**

- 문제 해결을 위해 Jackson 모듈 의존성(`jackson-datatype-jsr310`)을 추가하고, `@JsonFormat` 어노테이션을 사용해 날짜 직렬화를 시도했습니다. 또한, ModuleConfig 파일을 추가해`ObjectMapper`를 만들어 `JavaTimeModule`을 등록했으나 문제는 지속되었습니다.

### 4. **위기**

- `CacheManager`에서 JSON 직렬화 처리가 잘못되었다는 점을 쉽게 발견하지 못했고, 원인을 찾기 어려운 상황이었습니다. `GenericJackson2JsonRedisSerializer`를 이미 사용 중이었기 때문에 문제가 캐시 설정과 관련이 있다고 생각하지 않았습니다.

### 문제가 되던 원래 코드:

