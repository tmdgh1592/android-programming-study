# android-programming-study

# 4장
---
## UI 상태 유지하기

### ViewModel 의존성 추가하기

- Jetpack에 lifecycle-extension 포함되어 제공된다
- 새로 추가하는 의존성은 맨 끝에 넣는 것이 좋다

### ViewModel 추가하기

- ViewModel
    - 화면에 보여줄 데이터를 형식화하는 로직을 두기 좋은 곳
    - ViewModel을 사용하면 다른 방법을 사용하지 않아도 액티비티의 UI 상태 데이터를 메모리에 보존할 수 있다.
    - 액티비티가 종료될 때만 소멸된다
    - 액티비티 생명주기와 연동된다
    - 뷰모델은 액티비티를 참조하지 않는다 메모리 유실이 일어날 수 있기 때문에

### 프로세스 종료 시에 데이터 보존하기

- 사용자가 다른 앱으로 이동하거나 안드로이드 운영체제가 메모리를 회수할 때 앱의 프로세스는 안드로이드 운영체제에 의해 소멸된다.
- 그러면 이 프로세스가 갑자기 종료 된다면?
    - onStop에서 호출되는 SIS를 호출하자

viewModel vs SIS

- 다운로드 등 동적 작업에서 viewModel이 장점을 가짐
- 하지만 프로세스 종료되면 viewModel이 처리하지 못한다
- SIS는 디스크에 저장된다
- lifecycle-viewmodel-savedstate 라이브러리로 상태 데이터를 보존할 수 있음
- 소량의 정보 → SIS 대량의, 동적 → ViewModel

SIS , ViewModel 을 함께 사용하면 데이터가 절대 소멸되지 않는 것처럼 구현 가능하다

- 장기간 저장은 내부 저장소를 사용할 것