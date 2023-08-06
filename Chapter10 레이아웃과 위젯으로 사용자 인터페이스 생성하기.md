# Chapter10 레이아웃과 위젯으로 사용자 인터페이스 생성하기

## ConstraintLayout 
- 중첩된 레이아웃을 사용하는 대신 제약(constraint)들을 레이아웃에 추가한다.
- 제약은 두 개의 물걸은 연결해 끌어당기는 고무 밴드라고 생각하면 된다. 
- 뷰의 상하좌우 모두 제약을 생성할 수 있다. 
- 수평 또는 수직의 정반대 제약(opposing constraint)을 지정하면 정중앙에 위치 시킬 수 있다.

| 설정 유형  | 설정 값         | 용도 |
|--------|--------------|--|
| 고정 크기  | Xdp          | 변경되지 않는 크기로 뷰를 지정한다.(x는 고정 크기값), 크기 지정 단위는 dp다 |
| 내용에 맞춤 | wrap_content | 뷰에서 원하는 크기가 자동지정된다. |
| 제약에 맞춤 | 0dp          | 지정된 제약에 맞춰 뷰의 크기가 신축성 있게 조정된다. |

### Guideline

![GuideLine](https://developer.android.com/static/training/constraint-layout/images/guideline-constraint_2x.png)
- 앱 사용자에게 보이지 않는 수직 또는 수평의 가이드라인을 추가할 수 있다.
- 레이아웃에 가장자리를 기준으로 dp 단위 또는 백분율을 기준으로 레이아웃 내에 가이드라인을 배치할 수 있다.
- orientation을 사용하여 GuideLine의 vertical, horizontal을 정해줄 수 있다.
- `app:layout_constraintGuide_begin="20dp"`
  - begin, end로 GuideLine의 시작 부분을 명시해줄 수 있다.
- `app:layout_constraintGuide_percent="0.8"`
  - percent를 사용하는 요소는 따로 begin, end가 없고 만약 end 부분으로 20%만 여백을 주고싶다면 0.8 값을 주면된다.

### Barrier
![Barrier](https://developer.android.com/static/training/constraint-layout/images/barrier-constraint_2x.png)
- GuideLine과 유사하게 뷰를 제한할 수 있는 보이지 않는 선이지만, Barrier는 위치 자체를 정의하지 않는다.
- Barrier에 포함된 뷰들 중 가장 큰 크기를 가진 뷰에 크기가 맞춰진다.
- orientation을 사용하지 않고, `app:barrierDirection="end"`을 사용하여 정의 top, bottom, start 등이 있음
- `app:constraint_referenced_ids="text1, text2"` 을 통해 Barrier 안에 들어갈 뷰들을 지정해준다.

## MotionLayout
- ConstraintLayout을 확장한 것
- 뷰에 애니메이션을 쉽게 추가해준다.
- MotionScene 파일을 통해 애니메이션이 어떻게 수행되어야하는지 나타낸다.

