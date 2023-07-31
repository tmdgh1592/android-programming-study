# Chapter09 RecyclerView로 리스트 보여주기

## RecyclerView
- RecyclerView는 ViewGroup의 서브 클래스이며, 항목 뷰(item view)라고 하는 자식 View 객체들의 리스트를 보여준다.
- 각 항목 View는 RecyclerView의 행으로 나타나며 데이터 저장소에서 가져온 하나의 객체를 나타낸다.

## ViewHolder
- RecyclerView는 항목 View가 ViewHolder 인스턴스에 포함되어 있다고 간주한다.
- ViewHolder는 항목 View의 참조를 갖는다.
- 주로 데이터를 바인딩하는 역할을 한다.

## RecyclerView.Adapter
- 어댑터는 컨트롤러 객체로, RecyclerView와 RecyclerView가 보여줄 데이터 사이에 위치한다.
- 필요한 ViewHolder 인스턴스들을 생성한다.
- 모델 계층의 데이터를 ViewHolder들과 바인딩 한다.

### onCreateViewHolder()
- 보여줄 뷰를 인플레이트 한 후 이 뷰를 처리하는 ViewHolder 인스턴스를 생성하고 반환한다.

### onBindViewHolder()
- 인자로 전달된 위치에 있는 ViewHolder 인스턴스가 참조하는 뷰들에 데이터를 지정해준다.
- 주로 ViewHodler의 bind 함수를 사용해 데이터를 넣어준다. 

### getItemCount()
- 데이터가 저장된 데이터 셋에 몇 개의 데이터가 있는지 RecyclerView에게 알려준다.

### Adapter와 RecyclerView의 흐름
- RecyclerView가 Adapter에게 새로운 ViewHolder가 필요하다고 알림 (onCreateViewHolder() 호출)
- Adapter는 item view를 인플ㄹ에이트하고 ViewHolder 인스턴스를 생성하고 넘겨준다.
- RecyclerView는 인덱스에 저장된 데이터를 ViewHodler에 채워달라고 한다. (onBindViewHolder() 호출)
- Adapter는 데이터를 인덱스에 저장된 데이터 객체의 데이터를 ViewHolder에 넣는다.

## RecyclerView의 뷰 재활용
- 100개의 데이터를 표현한다고 해서 100개 모두 메모리에 가지고 있는 것은 아니다.
- 화면을 채우는데 충분한 개수만 item view를 생성하고 스크롤을 통해 화면을 벗어나면 벗어난 item view를 버리지 않고 재활용
- onCreateViewHolder()가 onBindViewHolder()보다 덜 호출 되는 이유이다.
- viewHolder를 재활용함으로써 시간과 메모리를 절약 한다.

## RecyclerView와 비슷한 ListView와 GridView
- 기존에는 ListView와 GridView를 사용해서 리스트나 그리드 형태로 항목들을 보여주었다.
- ListView가 있음에도 RecyclerView를 사용하는 이유는 다음과 같다.
  - 수평 방향으로 스크롤 가능한 ListView를 생성하는 기능은 ListView API에 포함 되어 있지 않아 많은 작업이 필요하다.
  - RecyclerView는 기본적으로 제공되는 애니메이션 효과가 있다. 물론 ListView 또는 GridView도 애니메이션을 적용 시킬 수는 있지만 복잡하다.

