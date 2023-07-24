package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    val isCheater: MutableMap<Question, Boolean> = mutableMapOf()
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val currentQuestion: Question
        get() = questionBank[currentIndex]

    init {
        Log.d(TAG, "ViewModel instance created")
        questionBank.forEach {
            isCheater[it] = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}
