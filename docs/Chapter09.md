# 9️⃣ RecyclerView로 리스트 보여주기

### ViewModel 생명주기

- ViewModel은 두 개의 상태(`생성됨` 과 `소멸되어 존재하지 않음`)만 갖는다.
- 액티비티와 함께 사용하면 액티비티 생명주기와 결합되고 프래그먼트와 함게 사용하면 프래그먼트 생명주기와 결합된다.
  - 즉, 프래그먼트의 뷰가 화면에 나타나 있는 한 ViewModel은 활성화된 상태를 유지하며, 장치 회전 시에도 유지되어 새로 생성된 프래그먼트 인스턴스를 이어서 사용할 수 있다.
  - 또한 ViewModel은 프래그먼트가 소멸할 때 같이 소멸한다. 예를 들어, 사용자가 백 버튼을 누르거나 호스팅 액티비티가 프래그먼트를 다른 것으로 교체할 때(`replace`) 해당 액티비티가 화면에 나타나 있더라도 프래그먼트 및 이것과 연관된 ViewModel은 더 이상 필요 없으므로 소멸한다.
  - 하지만 만약 액티비티가 현재 프래그먼트를 다른 것으로 교체할 때 트랜잭션이 백 스택에 추가된다면 해당 프래그먼트 인스턴스와 이것의 ViewModel은 소멸되지 않는다.

### RecyclerView의 LayoutManager

- RecyclerView는 항목들을 화면에 위치시키는 일을 직접 하지 않고 `LayoutManager` 에 위임한다.
- 그러면 `LayoutManager` 는 모든 항목들의 화면 위치를 처리하고 스크롤 동작도 정의한다.
- 따라서 `LayoutManager` 가 설정되지 않으면 RecyclerView의 작동이 바로 중단된다.

### RecyclerView

- RecyclerView는 ViewGroup의 서브 클래스이며, 항목 뷰(item view)라고 하는 자식 View 객체들의 리스트를 보여준다.
- 각 항목 View는 RecyclerView의 행으로 나타나며 데이터 저장소에서 가져온 하나의 객체를 나타낸다.
- 한 행으로 보이는 RecyclerView의 각 항목은 자신의 뷰 계층 구조를 가질 수 있다.

### ViewHolder

- RecyclerView는 항목 View가 ViewHolder 인스턴스에 포함되어 있다고 간주한다.

- ViewHolder는 항목 View의 참조(때로는 항목 뷰에 포함된 특정 위젯의 참조)를 갖는다.

- RecyclerView.ViewHolder 슈퍼클래스로부터 상속받은 itemView 속성은 생성자로 전달된 항목 View의 참조를 갖는다.

  ```kotlin
  class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
  		val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
  }
  ```

  ⇒ 여기서 itemView와 view는 동일한 항목 View의 참조 값을 갖는다.

- RecyclerView는 자체적으로 View를 생성하지 않으며, 항상 항목 View를 참조하는 ViewHolder를 생성한다.

- itemView가 참조하는 항목 View가 간단할 때는 ViewHolder가 할 일이 적다. 그러나 복잡해지면 ViewHolder가 항목 View의 서로 다른 부분(자식 View)을 아이템 객체에 더 쉽고 효율적으로 연결해준다.

### Adapter

- RecyclerView는 자신이 ViewHolder를 생성하지 않는다. 대신에 이 일을 **어댑터(adapter)**에 요청한다.
- 어댑터는 컨트롤러 객체로, RecyclerView와 RecyclerView가 보여줄 데이터 사이에 위치한다.
- 어댑터의 역할
  - 필요한 ViewHolder 인스턴스들을 생성한다.
  - 모델 계층의 데이터를 ViewHolder들과 바인딩한다.
- RecyclerView의 역할
  - 새로운 ViewHolder 인스턴스의 생성을 어댑터에게 요청한다 : `onCreateViewHolder` 호출
  - 저장된 위치의 데이터 항목에 ViewHolder를 바인딩하도록 어댑터에게 요청한다 : `onBindViewHolder` 호출
- 화면에 보여줄 객체 또는 이 객체가 저장된 List를 RecyclerView는 모르며, 어댑터가 알고 있다.

### 뷰의 재활용

- RecyclerView는 모든 항목 View를 생성하는 대신에 한 화면을 채우는 데 충분한 개수의 뷰만 생성해서 항목 View를 재활용한다.
- 이러한 이유로 `onCreateViewHolder` 가 `onBindViewHolder` 보다 덜 호출된다.
- 일단 충분한 수의 ViewHolder가 생성되면 RecyclerView는 `onCreateViewHolder` 의 호출을 중단하고 기존의 ViewHolder를 재활용해 `onBindViewHolder` 에 전달함으로써 시간과 메모리를 절약한다.

### ListView, GridView vs RecyclerView

- ListView, GridView 클래스는 리스트의 항목들을 스크롤하지만 각 항목에 관해서는 잘 알지 못한다.
- ListView, GridView에서는 ViewHolder 패턴 사용을 강제하지 않는다.
- 수평 방향으로 스크롤 가능한 ListView를 생성하는 기능은 ListView API에 포함되어 있지 않아 많은 작업이 필요하다.
- RecyclerView도 커스텀 레이아웃을 갖고 스크롤 가능하게 하는 것은 여전히 많은 작업이 필요하지만 기능 확장이 되도록 설계되었다.
- ListView, GridView와 달리 RecyclerView는 몇 가지 애니메이션 기능이 내장되어 있으며, 애니메이션 기능을 쉽게 커스터마이징할 수 있다.