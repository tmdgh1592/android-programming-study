# Chapter07 안드로이드 SDK 버전과 호환성

## 안드로이드 SDK 버전
- minSDK
- targetSDK
- compileSDK

## minSDK
- minSdkVersion 값은 앱을 설치하는 기준으로 삼는 최소한의 안드로이드 버전
- minSdkVersion 보다 낮은 버전의 안드로이드 장치에 앱을 설치하려하면 안드로이드가 거부

## targetSDK
- targetSdkVersion 값은 앱 설계에 사용된 API 레벨을 안드로이드에 알림
- 대부분의 경우 가장 최신의 안정화된 안드로이드 버전이 됨
- 구글 플레이 스토어에 신규 앱이나 앱의 업데이트를 올리고자 할 경우 targetSdk에 최저 제한이 있기 때문에 고려해서 정해야함

## compileSDK
- 안드로이드 스튜디오가 import 문으로 참조되는 클래스나 함수를 찾을 때 어떤 버전의 SDK에서 찾을 것인가를 이 버전으로 결정
- 가장 최신에 가까우면서 안정된 API 레벨을 선택하는것이 추천됨

## Jetpack 라이브러리
- 새로운 기능과 이에 대한 하위 호환성도 제공
  - 따라서 API 레벨에 따른 분기처리를 하지 않아도 된다.
  - Jetpack의 많은 AndroidX 라이브러리는 이전의 지원 라이브러리를 개선한 것이다.
  - 그치만 모든 기능이 포함되어 있는 것은 아니다.
