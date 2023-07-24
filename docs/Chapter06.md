# 6️⃣ 두 번째 액티비티 만들기

### 배울 내용

1. 새로운 액티비티를 생성하는 방법 
2. 액티비티에서 다른 액티비티를 시작하는 방법 
3. 액티비티 간에 데이터를 전달하는 방법

### tools 네임스페이스

- 디자인 시에 지정된 값이 미리보기에 나타나지만 앱을 실행할 때는 나타나지 않는다.
- 속성 예시
  - `tools:text=”정답”`

### 매니페스트(Manifest) 파일

- XML 파일이다.

- 안드로이드 운영체제에게 앱을 설명하는 메타데이터를 포함한다.

- 앱의 모든 액티비티는 안드로이드 운영체제가 알 수 있게 반드시 매니페스트에 선언해야 한다.

- 액티비티를 선언할 때는 `android:name` 속성이 필요하다.

  ```kotlin
  <activity android:name=".CheatActivity" />
  ```

  여기서 `.` 은 manifest 요소의 package 속성에 지정된 패키지에 `CheatActivity` 클래스가 위치함을 안드로이드 운영체제에게 알린다.

### startActivity(Intent)

- `startActivity(Intent)` 함수를 사용하면 한 액티비티에서 다른 액티비티를 쉽게 시작시킬 수 있다.

- ```
  startActivity(Intent)
  ```

   함수를 호출하면 이 호출은 안드로이드 운영체제에게 전달된다.

  - `startActivity(Intent)` 호출은 `ActivityManager` 라고 하는 안드로이드 운영체제의 컴포넌트로 전달된다.
  - `ActivityManager` 는 해당 액티비티의 인스턴스를 생성하고 이 인스턴스의 `onCreate(Bundle?)` 함수를 호출한다.
  - 어떤 액티비티를 시작시킬지에 대한 정보는 `Intent` 매개변수에 있다.

- 액티비티는 시작시키기에 앞서 

  ```
  ActivityManager
  ```

   는 시작시킬 액티비티 클래스가 매니페스트의 activity 요소에 선언되어 있는지 확인한다.

  - 만약 선언되어 있지 않으면 `ActivityNotFoundException` 이 발생되고 앱 실행이 중단된다.

### Intent

- 인텐트(Intent)는 컴포넌트가 운영체제와 통신하는데 사용할 수 있는 객체이다.

  - 여기서 컴포넌트는 안드로이드 4대 컴포넌트인 액티비티(Activity), 서비스(Service), 브로드캐스트 수신자(Broadcast Receiver), 콘텐트 제공자(Content Provider)를 의미한다.

- 인텐트는 다목적 통신 도구로, 이것을 추상화한 Intent 클래스는 인텐트의 용도에 따라 서로 다른 생성자들을 제공한다.

- 예시

  ```kotlin
  val intent = Intent(this, CheatActivity::class.java)
  ```

  `Intent(packaeContext: Context, class: Class<?>)`

  Intent 생성자에 전달하는 `Class` 인자에는 `ActivityManager` 가 시작시켜야 하는 액티비티 클래스를 지정한다.

  `Context` 인자는 이 액티비티 클래스가 있는 애플리케이션 패키지를 `ActivityManager` 에게 알려준다.

### 명시적 인텐트와 암시적 인텐트

- 명시적 인텐트
  - Context 객체와 Class 객체를 사용해서 생성하는 Intent는 **명시적(explicit) 인텐트**다.
  - **명시적 인텐트**는 앱 내부에 있는 액티비티를 시작시키기 위해 사용한다.
- 암시적 인텐트
  - 한 애플리케이션의 액티비티에서 다른 애플리케이션의 액티비티를 시작시킬 때 **암시적(implicit) 인텐트**를 생성한다.

### 인텐트 엑스트라(Intent Extra)

- 엑스트라는 호출하는 액티비티가 인텐트에 포함시킬 수 있는 임의의 데이터로, 생성자 인자로 생각할 수 있다. (액티비티 인스턴스는 안드로이드 운영체제에 의해 생성되고 그 생명주기가 관리된다.)
- 요청된 인텐트는 안드로이드 운영체제가 받아서 수신 액티비티에 전달하고 수신 액티비티는 해당 인텐트의 엑스트라로 전달된 데이터를 추출해 사용한다.
- 엑스트라는 키와 값이 한 쌍으로 된 구조로, `Bundle` 객체의 구조와 동일하다.

### 자식 액티비티로부터 결과 돌려받기

- 자식 액티비티로부터 데이터를 돌려받고 싶으면 

  ```
  startActivityForResult(Intent, Int)
  ```

   함수를 호출한다.

  - 첫 번째 매개변수는 `startActivity(Intent)` 에서의 Intent와 동일한 객체다.
  - 두 번째 매개변수는 **요청 코드(request code)**로 사용자가 정의한 정수다.
    - 요청 코드는 자식 액티비티에 전달되었다고 부모 액티비티가 다시 돌려받으며 부모 액티비티가 여러 타입의 자식 액티비티들을 시작시킬 때 어떤 자식 액티비티가 결과를 돌려주는 것인지 알고자 할 때도 사용된다.

- 부모 액티비티에 데이터를 돌려주기 위해 자식 액티비티에서 호출하는 함수에는 다음 두 가지가 있다.

  - `setResult(resultCode: Int)`

  - `setResult(resultCode: Int, data: Intent)`

  - 일반적으로 **결과 코드(result code)**는 사전 정의된 두 개의 상수, 즉 

    ```
    Activity.RESULT_OK
    ```

     또는 

    ```
    Activity.RESULT_CANCELED
    ```

     중 하나다.

    - 자식 액티비티가 어떻게 끝났는지에 따라 부모 액티비티에서 다른 액션을 취할 때 결과 코드를 사용하면 유용하다.

  - 자식 액티비티에서는 

    ```
    setResult(...)
    ```

     를 호출하지 않을 수도 있다.

    - 부모 액티비티에서 어떤 결과 코드인지 구분할 필요가 없거나 인텐트의 데이터를 받을 필요가 없다면, 안드로이드 운영체제가 기본 결과 코드를 전달하게 할 수 있다.

  - 자식 액티비티가 `startActivityForResult(...)` 로 시작되었다면 결과 코드는 항상 부모 액티비티에 반환된다.

  - 이때 자식 액티비티에서 `setResult(...)` 를 호출하지 않은 상태에서 사용자가 장치의 백 버튼을 누르면 부모 액티비티는 결과 코드로 `Activity.RESULT_CANCELED` 를 받게 된다.

### ActivityManager의 백 스택(back stack)

- 앱을 실행하면 안드로이드 운영체제는 앱을 시작시키는 것이 아니라 앱의 액티비티를 시작시킨다.
  - 정확히는 앱의 **론처(launcher)** 액티비티를 시작시킨다.
  - 론처 액티비티는 매니페스트에 **intent-filter** 요소로 정의된다.
- ActivityManager는 백 스택을 유지하며 백 스택은 앱의 액티비티만을 위한 것이 아니다.
- 모든 애의 액티비티들이 백 스택을 공유하며, 이때 앱이 아닌 안드로이드 운영체제에 있는 ActivityManager가 개입된다.