package com.example.codecasaquizapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.codecasaquizapp.R
import com.example.codecasaquizapp.ui.quiz.QuizActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for seconds before launching the MainActivity
        val delayMillis = 3000L
        findViewById<View>(android.R.id.content).postDelayed({
            Intent(this@SplashActivity , QuizActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }, delayMillis)
    }
}