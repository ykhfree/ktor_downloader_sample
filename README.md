# ktor 환경에서 kotlin flow를 이용한 파일 다운로드 구현

### 개발 및 빌드환경

* JDK 11
* kotlin 1.4.21
* ktor 1.5.0
* gradle wrapper 6.6.1
* Intellij Idea
* etc

### 설명
- 파일 다운로드 URL을 매개변수로 넘겨주면 해당 파일을 다운로드 받음.
- 이때 다운로드 progress percent를 stream 방식으로 보여줌.