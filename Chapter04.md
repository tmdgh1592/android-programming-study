# UI 상태 유지하기

장치 회전 후에 생성되는 액티비티 등 인스턴스가 파괴되고 다시 생성된다.
이러한 상황에서, 액티비티의 상태를 유지하기 위한 방법이 필요하다.

## ViewModel

Android Jetpack의 lifecycle-extentions 라이브러리에서 제공되는 ViewModel 클래스를 활용한다.
ViewModel은 특정 액티비티 화면과 연동되며, 해당 화면에 보여줄 데이터를 형식화하는 로직 두기 좋은 곳이다.
androidx.lifecycle 패키지는 **생명주기를 인식하는** 컴포넌트를 비롯해서 생명주기 관련 API도 제공한다.
ViewModel 또한 해당 패키지의 일부이다.

init 블럭 : ViewModel이 처음 생성될 때 호출된다.

onCleared() : ViewModel이 소멸될 때 호출된다. 클린업 할 것이 있으면 이 함수에서 하면 된다.

ViewModel을 생성하기 위해서 아래와 같이 코드를 작성할 수 있다.
```kotlin
private val viewModel: MainViewModel by lazy {
    ViewModelProvider(/* viewModelStoreOwner = */this)[MainViewModel::class.java]
}
```

장치 구성 변경으로 인해, Activity 인스턴스가 파괴되고 재생성된다고 하더라도 ViewModel 인스턴스가 새로 생성되지 않고 최초 생성되었던 인스턴스가 반환된다.
다만, 사용자가 백 버튼을 눌러서(또는 finish() 메서드 호출) Activity를 완전히 제거하는 경우 ViewModel도 onCleared() 메서드 호출과 함께 메모리에서 제거된다.

## ViewModel 생명주기와 ViewModelProvider
그렇다면, Activity가 완전히 제거되어야 하는 경우를 어떻게 판단할까?
Activity는 `isFinishing()` 라는 메서드를 제공한다.
만약 반환값이 true라면 Activity를 완전히 끝냈음을 의미한다. 따라서 액티비티 인스턴스가 소멸되더라도 당시 UI 상태를 보존할 필요가 없다.
반대로 반환값이 false라면 장치 회전에 따른 구성 변경으로 인해 시스템이 현재 액티비티 인스턴스를 소멸시킨다는 것을 의미하므로, UI 상태가 보존되어야 한다.
이 때 ViewModel을 사용하면 다른 방법을 사용하지 않아도 액티비티의 UI 상태 데이터를 메모리에 보존할 수 있다.

<img width="595" alt="스크린샷 2023-07-16 오후 7 49 57" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/97dc1f7d-cfc8-4471-9d64-dd595048ff70">

ViewModel과 Activity 관계는 단방향이다. (Activity <- ViewModel)
ViewModel은 액티비티나 다른 뷰의 참조를 가지면 안 된다. **Memory Leak**이 생길 수 있기 때문이다. (가령, ViewModel이 Activity context를 참조하고 있는 경우)
1. 장치 회전과 같은 상황에서 Activity가 제거되지 않아 메모리에 계속 남아 있는다.
   - IDE에서 ViewModel이 Context를 참조하고 있으면 메모리 누수 경고를 나타낸다. <img width="389" alt="스크린샷 2023-07-16 오후 7 59 05" src="https://github.com/tmdgh1592/android-programming-study/assets/56534241/94385be7-6073-4251-8473-57cc6d13cb20">
2. ViewModel이 현재 사용되지 않는 Activity를 참조하여 IllegalStateException 발생 (매번 발생하지는 않는다.)

## ViewModel에 데이터 추가하기
ViewModel은 사용하기 쉽도록 자신과 연관된 화면에서 필요한 모든 데이터를 저장하고 형식화한다.
프레젠테이션 로직 코드를 액티비티와 분리할 수 있어서 액티비티를 더 간단하게 유지할 수 있다.
액티비티에 추가되는 모든 코드는 의도치 않게 액티비티 생명주기에 영향을 받을 수 있기 때문에, 간단하게 화면을 나타내는 로직만 집중하고, 보여줄 데이터를 결정하는 내부 로직을 신경쓰지 않도록 한다.

ViewModel을 `val`, `by lazy` 키워드를 사용하여 초기화 하면 지연 초기화를 할 수 있을 뿐더러, 변경되지 않는 ViewModel을 가변 프로퍼티로 선언할 필요가 없다.
또한, 늦은 초기화 덕분에 onCreate()에서 사용되므로 안전하게 사용할 수 있다.

## 프로세스 종료 시에 데이터 보존하기
구성 변경 외에도 데이터 보존을 고려해야 하는 상황이 있다면 바로 프로세스가 종료되는 경우이다.
사용자가 다른 앱으로 이동하거나 안드로이드 OS가 메모리를 회수할 때 앱의 프로세스는 안드로이드 운영체제에 의해 소멸된다.
그럼 프로세스에 저장된 모든 객체들도 같이 소멸된다.
`STARTED`나 `RESUMED` 상태의 액티비티를 포함하는 프로세스는 다른 프로세스보다 높은 우선순위를 갖는다.
안드로이드 OS가 리소스를 회수할 때 우선순위가 낮은 프로세스를 먼저 선택하지만, 화면에 보이는 액티비티를 포함하는 프로세스는 리소스 회수가 되지 않기 때문이다.
만일 포그라운드 프로세스의 리소스가 회수된다면 이건 Device에 문제가 있는 것이다.

반대로, 사용자가 앱을 사용하다가 홈 버튼을 눌러 다른 앱에서 비디오를 보거나 게임을 한다면 `PAUSED`나 `STOPPED 상태가 되어 종료되기 좋은 프로세스가 된다. (이 때, 안드로이드 OS는 액티비티 하나만 소멸시키지 않고, 앱 프로세스 전체를 메모리에서 제거한다.)
문제는 액티비티나 ViewModel의 어떤 lifecycle callback 함수도 호출되지 않는다는 것이다.
이 상황에서 사용할 수 있는 적절한 방법은 **SIS(Saved Instance State, 저장된 인스턴스 상태)**이다.
SIS는 안드로이드 OS가 일시적으로 액티비티 외부에 저장하는 데이터이기 때문이다.

`onSaveInstanceState(Bundle)`의 bundle에 상태 값을 `key, value` 형태로 저장하여 관리한다.
`onRestoreInstanceState(Bundle)` 또는 `onCreate(Bundle?)`에서 저장한 값을 꺼내어 복원한다.
onCreate가 nullable한 이유는 액티비티가 최초 생성된 경우는 bundle이 null이기 때문이다.

## SIS와 액티비티 레코드
onSaveInstanceState(Bundle)이 호출될 때 데이터가 저장된 Bundle 객체는 안드로이드 OS에 의해 `액티비티 레코드(Activity Record)`로 저장된다.
액티비티가 보존(Stashed) 상태이면 액티비티 인스턴스가 존재하지 않지만, 액티비티 레코드 객체는 OS에 의해 살아있다.
따라서 안드로이드 OS는 액티비티 레코드를 통해 액티비티를 되살릴 수 있다.

onDestroy()가 호출되지 않고 보존 상태가 될 수 있다.
장치에 이상이 생기지 않는 한, onSaveInstanceState(Bundle)이 호출되는 것에 의존하여 코드를 작성하면 된다.
이 함수가 실행된 후에는 언제든 해당 액티비티가 소멸될 수 있기 때문이다.

액티비티 레코드는 `액티비티가 종료` 또는 `장치 재부팅`되면 같이 소멸된다. (Bundle 마찬가지로 소멸)

## ViewModel vs SIS
SIS만 사용해도 충분한데 ViewModel도 같이 사용해야 할까?
ViewModel의 진가는 서버에서 데이터를 불러오는 등의 비동기 작업에서 진가를 발휘한다.
액티비티에서 데이터를 불러오다가 구성 변경이 발생하면 데이터를 불러오는 작업이 종료되지만, ViewModel의 lifecycle은 Activity보다 길기 때문에 작업을 계속할 수 있다.
SIS가 주목받는 이유는 프로세스가 종료되면 ViewModel이 처리하지 못하기 때문이다.
다만 SIS는 Serialized되어 디스크에 저장되므로 크고 복잡한 객체 저장은 피해야 한다.
`lifecycle-viewmodel-savedstate` 라이브러리를 사용하면 프로세스가 종료될 때 ViewModel이 자신의 상태 데이터를 보존할 수 있게 해준다. 따라서 두 가지를 절충해서 사용하면 된다.

- SIS 사용 : UI 상태를 다시 생성하기 위해 필요한 소량의 정보
- ViewModel 사용 : 장치 구성 변경으로 UI에 필요한 많은 데이터를 쉽고 빠르게 접근하고자 메모리에 캐싱할 때

만약, SIS와 ViewModel을 함께 사용하는데, 예를 들어, 장치 구성 변경 후에 SIS 데이터를 사용해서 ViewModel을 변경하면 앱에서 불필요한 작업을 할 수도 있다.
ViewModel에서 동일한 상태에 대해 동일한 결과를 반환하는 함수가 호출된다면, 상태를 체크하고 다른 경우에만 수행하는 것이 성능에 도움이 된다.
