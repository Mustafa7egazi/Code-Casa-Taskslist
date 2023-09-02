package com.example.codecasaquizapp.ui.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.codecasaquizapp.databinding.ActivityQuizBinding
import com.example.codecasaquizapp.ui.score.ScoreActivity
import com.example.codecasaquizapp.viewmodel.TriviaViewModel
import java.io.IOException
import java.net.SocketTimeoutException

class QuizActivity : AppCompatActivity() {

    private val viewModel: TriviaViewModel by lazy {
        ViewModelProvider(this)[TriviaViewModel::class.java]
    }

    private var questionsStructure = mutableMapOf<String, List<String>>()
    private var selectedChoices = mutableMapOf<String, String>()
    private var correctAnswers = mutableMapOf<String, String>()
    private var currentQuestionIndex = 1
    private var questionsCount: Int? = null
    private lateinit var selection: String


    private lateinit var binding: ActivityQuizBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try {
            viewModel.getSetOfQuestions()
            observingData()

        } catch (e: IOException) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        } catch (e: SocketTimeoutException) {
            finish()
            startActivity(intent)
        }


        binding.nextBtn.setOnClickListener {
            goToNextQuestion()
        }

        binding.previousBtn.setOnClickListener {
         goToPreviousQuestion()
        }

        binding.submitBtn.setOnClickListener {
            submitTheQuiz()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun goToNextQuestion() {
        val selectionId = binding.choicesRadioGroup.checkedRadioButtonId
        if (selectionId == -1) {
            Toast.makeText(this, "Please answer the question first", Toast.LENGTH_SHORT).show()
        } else {
            if (selectedChoices.size < 10) {

                selection = findViewById<RadioButton>(selectionId)?.text.toString()
                selectedChoices["Q.$currentQuestionIndex"] = selection
                currentQuestionIndex++
            }

            if (currentQuestionIndex <= questionsCount!!) {
                binding.questionNumberTV.text = "Q.$currentQuestionIndex"
                val questionData = questionsStructure["Q.$currentQuestionIndex"]

                binding.questionHeaderTV.text = questionData?.last()
                binding.firstChoiceRB.text = questionData?.get(0) ?: "null"
                binding.secondChoiceRB.text = questionData?.get(1) ?: "null"
                binding.thirdChoiceRB.text = questionData?.get(2) ?: "null"
                binding.fourthChoiceRB.text = questionData?.get(3) ?: "null"
                binding.choicesRadioGroup.clearCheck()
            } else {
                Toast.makeText(this, "Last question", Toast.LENGTH_SHORT).show()
            }
        }

        if (questionsCount!! > 1 && currentQuestionIndex > 1) {
            viewModel.showPreviousBtn.value = true
        }

        if (currentQuestionIndex == questionsCount!! && binding.submitBtn.visibility != View.VISIBLE) {
            binding.submitBtn.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun goToPreviousQuestion() {
        currentQuestionIndex--

        if (currentQuestionIndex < 10) {
            binding.submitBtn.visibility = View.INVISIBLE
        }
        if (currentQuestionIndex == 1 ){
            viewModel.showPreviousBtn.value = false
        }

        if (currentQuestionIndex >= 1) {
            binding.questionNumberTV.text = "Q.$currentQuestionIndex"
            val questionData = questionsStructure["Q.$currentQuestionIndex"]

            binding.questionHeaderTV.text = questionData?.last()
            binding.firstChoiceRB.text = questionData?.get(0) ?: "null"
            binding.secondChoiceRB.text = questionData?.get(1) ?: "null"
            binding.thirdChoiceRB.text = questionData?.get(2) ?: "null"
            binding.fourthChoiceRB.text = questionData?.get(3) ?: "null"

            var selectionId: Int? = null
            val currentSelectedAnswer = selectedChoices["Q.$currentQuestionIndex"]

            for (i in 0 until binding.choicesRadioGroup.childCount) {
                val radioButton = binding.choicesRadioGroup.getChildAt(i) as RadioButton
                if (radioButton.text == currentSelectedAnswer) {
                    selectionId = radioButton.id
                    break // Exit the loop when a match is found
                }
            }

            if (selectionId != null) {
                findViewById<RadioButton>(selectionId).isChecked = true

            } else {
                Toast.makeText(this, "selectedId is null", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT).show()
        }

    }

    private fun submitTheQuiz() {
        if (!selectedChoices.containsKey("Q.$currentQuestionIndex")) {
            val selectionId = binding.choicesRadioGroup.checkedRadioButtonId
            if (selectionId == -1) {
                Toast.makeText(this, "Please answer the last question", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (currentQuestionIndex <= 10) {
                    selection = findViewById<RadioButton>(selectionId)?.text.toString()
                    selectedChoices["Q.$currentQuestionIndex"] = selection
                }


                var index = 1
                var score = 0
                for (i in selectedChoices) {

                    if (i.value == correctAnswers["Q.$index"]) {
                        score++
                    }
                    index++
                }

                Log.d("7egzz", "onCreate: SCORE-> $score")

                Intent(this, ScoreActivity::class.java).also {
                    it.putExtra("score", score)
                    startActivity(it)
                    finish()
                }


            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observingData(){
        viewModel.questionsDataResponse.observe(this) { listOfQuestionsResponse ->
            if (listOfQuestionsResponse != null) {
                questionsCount = listOfQuestionsResponse.size
                binding.textView.visibility = View.VISIBLE
                var qNum = 1
                for (questionItem in listOfQuestionsResponse) {
                    val questionHeader = questionItem.question.text
                    val correctAnswer = questionItem.correctAnswer
                    val choicesWithQuestionHeader =
                        (listOf(questionItem.correctAnswer) + questionItem.incorrectAnswers)
                            .shuffled()
                            .toMutableList()

                    choicesWithQuestionHeader.add(questionHeader)

                    questionsStructure["Q.$qNum"] = choicesWithQuestionHeader
                    correctAnswers["Q.$qNum"] = correctAnswer

                    qNum++
                }

                Log.d("7egzz", "onCreate: $correctAnswers")


                binding.quizLoading.visibility = View.GONE
                binding.questionNumberTV.text = "Q.1"
                val firstQuestionData = questionsStructure["Q.1"]

                binding.questionHeaderTV.text = firstQuestionData?.last()
                binding.firstChoiceRB.text = firstQuestionData?.get(0) ?: "null"
                binding.secondChoiceRB.text = firstQuestionData?.get(1) ?: "null"
                binding.thirdChoiceRB.text = firstQuestionData?.get(2) ?: "null"
                binding.fourthChoiceRB.text = firstQuestionData?.get(3) ?: "null"


                binding.previousBtn.visibility = View.INVISIBLE
                binding.questionStructureLayout.visibility = View.VISIBLE
            } else {
                binding.quizLoading.visibility = View.GONE
                binding.textView.apply {
                    text = "Received null response"
                    visibility = View.VISIBLE
                }
            }
        }

        viewModel.showPreviousBtn.observe(this) {
            if (it) {
                binding.previousBtn.visibility = View.VISIBLE
            } else {
                binding.previousBtn.visibility = View.INVISIBLE
            }
        }
    }
}