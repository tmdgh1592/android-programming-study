# 안드로이드 앱의 디버깅

안드로이드에서는 프로젝트의 문제점을 살펴볼 수 있는 방법은 크게 4가지가 있다.
- Log 설정하기
- BreakPoint 설정하기
- Lint 사용하기
- 프로파일러 살펴보기

## 예외와 스택 기록

안드로이드 스튜디오 메인 창 아래쪽 테두리에 `Logcat` 도구 버튼을 통해 발생한 에러의 StackTrace를 추적할 수 있다.
메시지가 너무 많기 때문에, 상황에 따라 적절히 필터링할 필요가 있다.

- <img width="404" alt="스크린샷 2023-07-17 오전 10 23 10" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/4f939891-9f34-49ed-bba9-d5f6df999e34">
- <img width="371" alt="스크린샷 2023-07-17 오전 10 23 18" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/3dfeb652-30c4-452c-badb-253c8c44a4b3">

위와 같이 `현재 실행 중인 기기`를 선택할 수 있으며, `tag`, `level` 등을 설정하여 Log에 대한 정보를 필터링하여 확인할 수 있다.
만약, Exception에 대해서만 확인하고 싶다면, level을 Error로 지정하고, 검색 상자에 `fatal exception을 입력한다.
파란색으로 하이라이팅 된 로그를 살펴보면 소스 코드의 해당 줄로 이동할 수 있다.

## 중단점 설정하기

중단점(breakpoint)는 설정된 해당 라인이 실행되기 전에 실행을 중지시켜서 그 다음부터 어떻게 실행되는지 코드를 한 줄씩 살펴볼 수 있게 해준다.
원하는 라인에서 Command + F8 키를 눌러 중단점을 지정/해제할 수 있다.

<img width="402" alt="스크린샷 2023-07-17 오전 10 29 20" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/deda86a2-b008-4167-b751-51a5e4744203">

<img width="947" alt="스크린샷 2023-07-17 오전 10 31 12" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/3712e110-8493-4878-8e9a-74c53a117ee5">

만약 프로그램을 계속 진행하고 싶다면 맨 왼쪽 아이콘인 Resume Program 아이콘을 실행한다.
오른쪽 큰 창에는 여러 데이터들이 보여지고 있다.
이 화면에서 문제가 있다고 생각하는 변수의 값을 확인하여 정상적으로 업데이트 되었는지 등을 확인할 수 있다.

## Lint

Lint는 안드로이드 코드의 정적 분석기(static analyzer)이다.
정적 분석기란, 앱을 실행하지 않고 코드를 검사해 결함을 찾는 프로그램이다.
Lint는 안드로이드 프레임워크에 관한 지식을 사용해 사전에 코드를 더 깊이 있게 살펴보고, 컴파일러가 알 수 없는 문제를 찾아주기 때문에 대부분의 경우 Lint의 조언은 받아들일 만한 가치가 있다.

`Code` -> `InsepctCode`를 클릭하고 `Whole project`를 선택한 후 OK 버튼을 클릭하면 Lint가 실행된다.

<img width="330" alt="스크린샷 2023-07-17 오전 10 39 25" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/769549e7-35d2-4ce7-a9e9-2ceec77dd7eb">
<img width="579" alt="스크린샷 2023-07-17 오전 10 40 09" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/2d02a12f-2cea-437c-b27f-35a3d9a47c30">

위 사진은 Lint를 사용했을 때, Android -> Lint -> Internationalization의 한 예시이다.
자세히 살펴보면, `Hardcoded text`가 문제이다.
앱을 글로벌하게 출시한다면 텍스트를 하드 코딩하는 것보다 strings.xml을 통해 관리하여 translatable하게 만들어 줄 필요가 있다.
이 외에도 LTR, RTL 등에 대해서도 수정을 제안해주기도 하며, 불필요한 리소스를 제거하라고 추천해주는 등 다양한 제안이 있다.

## 프로파일러 살펴보기

<img width="1639" alt="스크린샷 2023-07-17 오전 10 43 36" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/4ad30fce-b602-4ab4-9d29-029e6f3bb552">

프로파일러를 살펴보면, 현재 앱이 사용하는 CPU, Memory, 네트워크, 배터리 사용량 등에 대해 자세히 알 수 있다.

<img width="237" alt="스크린샷 2023-07-17 오전 10 44 43" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/a2ecedae-cbb6-4f47-ab7a-2d30c0e68c24">

예를 들어, 프로파일러를 통해 앱이 사용하는 CPU 퍼센테이지와 다른 앱들이 사용하는 CPU 퍼센테이지를 상대적으로 비교할 수 있다.
또한, 앱이 사용하고 있는 Thread 개수가 몇 개인지 보여주기도 한다.

이 외에 Memory 메트릭을 확인하여, 메모리가 어떤 화면, 동작에서 많이 사용되는지 등을 분석하여 앱을 최적화하는 데에 도움이 될 수 있다.
