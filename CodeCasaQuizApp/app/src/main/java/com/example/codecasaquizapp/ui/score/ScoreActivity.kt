package com.example.codecasaquizapp.ui.score

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.codecasaquizapp.R
import com.example.codecasaquizapp.databinding.ActivityScoreBinding
import com.example.codecasaquizapp.ui.quiz.QuizActivity

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding:ActivityScoreBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra("score",-1)
       if (score != -1){
           if (score <= 5){
               binding.scoreMessageTV.text = "Seek for increasing you knowledge.\n You got $score out of 10"
               binding.scoreAnimation.setAnimation(R.raw.increase_info_anim)
           }else{
               binding.scoreMessageTV.text = "Well done, You got $score out of 10"
               binding.scoreAnimation.setAnimation(R.raw.well_done_anim)
           }
       }else{
           Toast.makeText(this,"-1",Toast.LENGTH_SHORT).show()
       }

        binding.takeQuizBtn.setOnClickListener {
            Intent(this,QuizActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding.exitBtn.setOnClickListener {
            finish()
        }
    }
}