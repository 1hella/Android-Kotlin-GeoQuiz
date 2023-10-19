package com.wanhella.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val NUM_CHEATS_KEY = "NUM_CHEATS_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var userAnswers = arrayOf<Boolean?>(null, null, null, null, null, null)

    private var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val numQuestions: Int
        get() = questionBank.size

    var isCheater: Boolean
        get() = savedStateHandle[IS_CHEATER_KEY + currentIndex] ?: false
        set(value) {
            savedStateHandle.set(IS_CHEATER_KEY + currentIndex, value)
            if (value) {
                numCheats -= 1
            }
        }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val numQuestionsAnswered: Int
        get() {
            var count = 0
            for (i in userAnswers) {
                if (i != null) {
                    count += 1
                }
            }
            return count
        }
    val numQuestionsCorrect: Int
        get() {
            var numCorrect = 0
            for (i in 0 until numQuestions) {
                if (questionBank[i].answer == userAnswers[i]) {
                    numCorrect += 1
                }
            }
            return numCorrect
        }

    var currentQuestionUserAnswer: Boolean?
        get() = userAnswers[currentIndex]
        set(value) = userAnswers.set(currentIndex, value)

    var numCheats: Int
        get() = savedStateHandle[NUM_CHEATS_KEY] ?: 3
        set(value) = savedStateHandle.set(NUM_CHEATS_KEY, value)

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }
}