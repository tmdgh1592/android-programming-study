# Chapter05 안드로이드 앱의 디버깅

## 안드로이드 스튜디오에서 제공하는 디버깅 방법 3가지

1. 로그캣(LogCat)
2. 안드로이드 Lint
3. 디버거

### 로그캣 활용하기
```kotlin
 private fun updateQuestion() {
    Log.d(TAG, "Updating question text", Exception())
    val questionTextRestId = quizViewModel.currentQuestionText
    questionTextRestId.setText(questionTextRestId)
}
```

`Log.d(String, String, Throwable)`로 전달하는 예외는 이미 발생한 예외가 되면 안된다.
<br>
따라서 새로운 Exception을 생성해 함수에 전달하면 된다. 그러면 이 예외가 생성되었던 곳을 알 수 있다.

### 디버거
실행 중인 앱을 다시 시작하지 않고 디버깅해야 하는 경우가 있다. 이때는 실행 중인 앱에 디버거를 연결하면 된다.
<br>
단 디버거가 연결될 때 부터 중단점이 활성화되므로, 디버거 연결 전에 실행되어 지나쳤던 중단점들은 무시된다.

### 안드로이드 Lint
안드로이드 Lint는 안드로이드 코드의 `정적 분석기(static analyzer)`다. 
<br>
정적 분석기란, 앱을 실행하지 않고 코드를 검사해 결함을 찾는 프로그램이다.
Lint는 안드로이드 프레임워크에 관한 자신의 지식을 사용해 ㅅ전에 코드를 더 깊이 있게 살펴보고, 컴파일러가 알 수 없는 문제를 찾는다.
<br>
그러므로 대부분의 경우에 Lint에 조언은 받아들일 많한 가치가 있다. 
