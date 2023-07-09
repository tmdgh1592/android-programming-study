package com.example.geoquiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    // 토스트 메시지의 Gravity 설정은 R - Api level 30 버전 이상 부터 적용이 안됩니다. (코드로 명시했다 하더라도)
    // 안드로이드에선 토스트 메시지 보단 스낵바 사용을 권장

    /**
     * 토스트 메시지 사용의 대안
     * 앱이 포그라운드에 있다면 토스트 메시지 대신 스낵바를 사용하는 것이 좋습니다.
     * 스낵바에는 사용자가 실행할 수 있는 옵션이 포함되어 있으며 이를 통해 더 나은 앱 환경을 제공할 수 있습니다.
     * -> 스낵바에 경우 사용자와 상호작용할 수 있는 요소가 있음
     * 앱이 백그라운드에 있고 사용자가 어떤 조치를 취하게 하려면 토스트 메시지 대신 알림을 사용하세요.
     */
}
