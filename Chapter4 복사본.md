# android-programming-study

# 5장
---
# 안드로이드 앱의 디버깅

결함이 있을 때 LogCat, Lint,debugger의 사용법을 알아보자

Verbose → Error → fatal exception을 입력하면 에러 문구를 찾을 수 있다.

코틀린 예외들은 내부적으로 java lang 패키지의 예외 클래스들과 연관된다.

- [koltin.Run](http://koltin.Run)timeException은 UninitializedPropertyAccessException의 슈퍼 클래스임


런타임시 사용자 입력이 작동 안한다면?
    - Logging을 사용할 수 있다

중단점 설정하기

- Run → Debug app
- 앱이 실행 중일 때 “안드로아드 프로세스에 디버거 연결” 통해 실행 중일 때 디버깅이 가능하다

안드로이드 Lint 

- 앱을 실행하지 않고 코드를 검사하는 프로그램
- XML 객체도 타입 검사 가능하다.
- 안드로이드 버전이 업그레이드 되면서 기본적으로 검사하는 린트도 많아진다.
    - 아래 경고 문구가 그 예시

프로젝트 빌드 문제 해결

- 리소스 파일들의 XML 코드가 제대로 작성되었는지 재확인한다.
- 프로젝트를 처음부터 다시 빌드한다
- 그래들을 사용해 현재 설정에 맞게 프로젝트를 동기화한다
- 안드로이드 Lint를 실행한다