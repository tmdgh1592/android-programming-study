# Chapter04 UI 상태 유지하기

## GeoQuiz 앱의 두가지 결함

1. `Runtime Configuration Change(구성 변경)`에 따른 UI 상태가 유실됨
2. 프로세스 종료에 따른 UI 상태 유실

---

### ViewModel(뷰모델)
- `ViewModel`은 특정 액티비티 화면과 연동되며, 해당 화면에 보여줄 데이터를 형식화 하는 로직을 두기 좋은 곳이다.
- `모델(Model)` 데이터를 `화면(View)`에 보여주는 역할을 한다.

### ViewModelProvider(뷰모델 프로바이더)
Activity 인스턴스가 처음으로 ViewModel을 요청하면 ViewModelProvider가 새로운 ViewModel 인스턴스를 생성하고 반환한다.
<br>
- 장치 구성변경이 일어나면? 
  - 새로 생성된 Activity 인스턴스가 ViewModel을 또 요청하면, 새로 생성하지 않고 이미 있던 ViewModel인스턴스가 반환된다.
- Activity 인스턴스가 종료되어 소멸 될 때는 VieModel 인스턴스도 같이 메모리에서 제거된다.

### 그럼 내부에서 이 두 경우를 어떻게 분기처리를 하는가?
장치 구성변경이 일어나거나 뒤로가기 버튼을눌러 액티비티를 아에 종료시키던 똑같이 Activity 인스턴스가 소멸된다.
<br>
이 두 가지는 Activity의 `isFinishing` 속성으로 분기처리 한다.
`isFinishing`이 `true`라면 액티비티를 종료시키려는 목적이기 때문에 UI 상태를 가지고 있는 `ViewModel`의 인스턴스가 남아있을 필요가 없다.
<br>
따라서 `ViewModel` 인스턴스도 같이 소멸된다.
<br>
<br>
반대로 `isFinishing`이 `false`라면 장치 구성변경을 통한 Activity 인스턴스 소멸이기 때문에 UI 상태를 가지고 있어야한다.
<br>
따라서 `ViewModel` 인스턴스는 남아야한다.

#### 주의!
- Activitiy와 ViewModel 간의 관계는 단방향이다. 즉, 액티비티는 ViewModel을 참조하지만, ViewModel을 액티비티를 참조하지 않는다.
- **ViewModel은 액티비티나 다른 뷰의 참조를 가지면 안 된다. 소멸 되어야하는 객체의 참조를 다른 객체가 가지면 메모리 유실이 생기기 때문이다.**
- 참조되는 객체를 가비지 컬렉터가 메모리에서 제거할 수 없게 된다.(이것을 강한 참조(Strong Reference)라고 한다.)
- `ViewModel`이 `Activity` 인스턴스를 가지면 다음과 같은 문제 사항이 생긴다.
- 액티비티 인스턴스가 메로리에서 제거되지 않아서 인스턴스가 사용하는 메모리가 유실된다.
- ViewModel 인스턴스가 현재 사용되지 않는 과거 액티비티의 참조를 갖게 되어 ViewModel 인스턴스가 과거 액티비티의 뷰를 변경하려고 하면 IllegalStateException이 발생한다.

---

### 프로세스 종료 시에 데이터 보존하기
장치의 구성 변경이 생길 때만 안드로이드 운영체제가 액티비티 인스턴스를 소멸시키는 것은 아니다.
<br>
각 앱은 프로세스로 실행되며, 프로세스는 UI관련 작업을 실행하는 하나의 스레드와 앱의 객체들을 저장하는 메모리를 포함한다.
<br>
사용자가 다른 앱으로 이동하거나 안드로이드 운영체제가 메로리를 회수할 때 앱의 프로세스는 안드로이드 운영체제에 의해 소멸된다.
<br>
앱의ㅣ 프로세스가 소멸될 때는 이 프로세스에 저장된 모든 객체들도 같이 소멸된다.
<br>
안드로이드 운영체제가 앱의 프로세스를 소멸시킬 때는 메모리에 잇는 앱의 모든 액티비티들과 ViewModel들이 제거되지만, 그 어떤 생명주기 콜백 함수도 호출하지 않는다.
<br>
그럼 다시 본론으로 돌아가서 프로세스 종료 시에 데이터 보존은 어떻게 할까? 
<br>
바로 `SavedInstanceState(저장된 인스턴스 상태, SIS)`에 데이터를 저장하는 것이다.
<br>
`SIS`는 안드로이드 운영체제가 일시적으로 액티비티 외부에 저장하는 데이터이며, `Activity.onSaveInstanceState(Bundle)`을 오버라이드해 `SIS`에 데이터를 추가할 수 있다.
액티비티가 `중단` 상태로 바뀔 때는 언제든지 안드로이드 운영체제가 `Activity.onSaveInstanceState(Bundle)`을 호출한다.

### onSaveInstanceState(Bundle) 오버라이드하기
액티비티의 슈퍼 클래스에 기본 구현된 `onSaveInstanceState(Bundle)`에서는 현재 액티비티의 모든 뷰가 자신들의 상태를 `Bundle` 객체에 저장된 뷰들의 상태 데이터를 사용해서 액티비티의 뷰 계층을 다시 생성한다.
<br>
액티비티에서 `onSaveInstanceState(Bundle)`을 오버라이드하면 추가적으로 `Bundle` 객체에 데이터를 저장할 수 있으며, 이 데이터는 onCreate(Bundle?)에서 다시 받을 수 있다. 

### SIS와 액티비티 레코드
`onSaveInstanceState(Bundle)`이 호출될 때 데이터가 저장된 `Bundle` 객체는 안드로이드 운영체제에 의해 액티비티의 `액티비티 레코드(Activity Record)`로 저장되기 때문에 액티비티가 소멸해도 데이터가 존속될 수 있다.
<br>
장치에 이상이 있지 않은 이상 `onStop()`과 `onSaveInstanceState(Bundle)`이 호출되는 것에 의존해서 코드를 작성하면 된다.
<br>
일반적으로는 현재 액티비티에 속하는 작고 일시적인 상태 데이터를 `Bundle`객체에 보존하기 위해 `onSaveInstanceState(Bundle)`을 오버라이드 한다.
<br>
그리고 지속해서 저장할 데이터는 `onStop()`을 오버라이드해서 처리한다. 이 함수 호출 이후에는 언제든 해당 액티비티가 소멸될 수 있기 때문이다.
<br>
액티비티 레코드는 액티비티가 종료될 때 같이 소멸된다.

### ViewModel vs SIS
사실 따지고만 보면 프로세스가 종료될 경우, 장치의 구성변경이 생기는 경우 둘 다 커버할 수 있는 SIS만 사용해도 충분할 것 같은데 왜 굳이 `ViewModel`을 사용할까?
<br>
`ViewModel`의 진가는 비동기적 처리(서버로 부터 데이터가져오기)에서 발휘된다. 
<br>
`ViewModel`은 장치의 구성 변경이 생겨도 다운로드 작업을 계속할 수 있게 해준다. 
<br>
UI 상태를 다시 생성하기 위해 필요한 소량의정보를 저장할 때는 `SIS`를 사용하고,<br>
장치의 구성 변경이 생겨서 UI에 넣는 데 필요한 많은 데이터에 빠르고 쉽게 접근하고자 메모리에 캐싱할 때는 `ViewModel`을 사용한다.


