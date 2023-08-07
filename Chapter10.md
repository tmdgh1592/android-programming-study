# 레이아웃과 위젯으로 사용자 인터페이스 생성하기

- LinearLayout과 같이 레이아웃 중첩을 야기하는 ViewGroup은 코드 파악과 변경을 어렵게하며, 앱의 성능도 저하된다.
  - 중첩된 레이아웃을 안드로이드 OS가 처리하고 보여주는데 시간이 오래 걸리기 때문이다.
 
## ConstraintLayout
- ConstraintLayout처럼, 반면 중첩되지 않은 레이아웃은 빠르게 처리해서 보여준다.
  - ConstraintLayout은 제약(Constarint)들을 사용하여 레이아웃을 배치한다.
- 위젯의 크기 조정을 위한 세 가지 방법
  - 내용에 맞춤(wrap_content) : 뷰에서 '원하는 크기'가 자동 지정된다. TextView의 경우 이것의 내용을 보여주는데 충분한 정도의 크기임을 의미한다.
  - 고정 크기(Xdp) : 변경되지 않는 크기로 뷰를 지정한다(X는 크기 값). 크기 지정 단위는 dp다.
  - 제약에 맞춰 위젯이 자동으로 확장(match_contraint)
- 기존의 LinearLayout 등을 ConstarintLayout으로 변경하길 권장하고 있기 때문에, Android Studio에서 `Component Tree`의 ViewGroup을 우클릭하면 ConstraintLayout으로 자동 변환이 가능하다.
  - 다만, 기능이 완벽하지 않기 때문에, 변환 후 일부 수정이 필요하기는 하다.
- Constraint Handle : ConstraintLayout의 자식들은 상하좌우로 다른 View에 제약을 걸 수 있다.
- 레이아웃 매개변수(layout paramter) : ConstarintLayout에서 layout_xxx로 이루어져 있는 속성을 의미한다.

## 스타일, 테마, 테마 속성
- 스타일(Style)은 위젯이 어떻게 보이고 작동하는지를 나타내는 속성들을 갖는 XML 리소스다.
- 예를 들어, 다음은 보통보다 더 큰 크기의 텍스트 위젯을 구성하는 스타일 리소스다.
```xml
<style name="BigTextStyle">
    <item name="android:textSize">20sp</item>
    <item name="android:padding">3dp</item>
</style>
```
- `@style/BigTextStyle`로 접근할 수 있다.
- 만약 프로젝트에서 중복되고 정해져 있는 스타일이 있다면, 매번 지정하지 않고 Style을 만든다면 한 번에 수정하기도, 코드를 작성하기도 편할 것이다.
- TextView와 같은 기본 위젯은 안드로이드가 생성한 스타일을 참조하는 style 속성을 갖는다.

- 테마 속성 참조
   - 앱의 테마 스타일을 위젯에 적용할 수 있다.
   - 안드로이드의 런타임 리소스 매니저에게 `이 앱의 테마에 가서 listSeparatorTextViewStyle이라는 이름의 속성을 찾아라. 이 속성은 다른 스타일 리소스를 가리킨다. 이 리소스의 값을 가져와서 여기에 넣어라.` 라고 알려주는 것이다.

- 궁금증 해소하기: 마진 vs 패딩
  - 마진 속성은 레이아웃 매개변수이며 위젯들 간의 간격을 결정한다. 따라서 위젯 자신은 마진을 알 수 없고, 당연히 해당 위젯의 부모가 마진을 처리해야 한다.
  - 패딩 속성은 레이아웃 매개변수가 아니다. 패딩은 위젯이 갖고 있는 콘텐츠보다 자신이 얼마나 더 커야 하는지를 나타낸다.
- 궁금증 해소하기: ConstraintLayout의 다른 기능
  - `가이드라인` 같은 도우미 뷰를 포함하고 있다. 가이드라인은 앱 화면에 나타나지 않으며 뷰를 배치하는 데 도움을 제공한다. 수평, 수직 방향 두 가지가 있으며, dp값 또는 비율(%)를 지정하여, 자식 View들이 특정 위치를 넘지 않도록 제약을 걸 수 있다.
  - MotionLayout은 Constraint을 확장한 것이며, 애니메이션을 쉽게 추가해준다. MotionLayout을 사용하려면 MotionScene 파일을 생성한다. 이 파일은 애니메이션이 어떻게 수행되어야 하는지, 그리고 시작과 끝 레이아웃의 어떤 뷰들이 상호 연관되는지를 나타낸다. 그리고 Keyframe을 정의해서 애니메이션에 개입되는 뷰들도 정의할 수 있다.
