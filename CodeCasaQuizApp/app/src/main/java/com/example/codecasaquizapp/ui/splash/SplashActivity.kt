package com.example.codecasaquizapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codecasaquizapp.R
import com.example.codecasaquizapp.ui.quiz.QuizActivity
import com.example.codecasaquizapp.util.isInternetAvailable

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for seconds before launching the MainActivity
        val delayMillis = 3000L
       findViewById<View>(android.R.id.content).postDelayed({
            if (isInternetAvailable(this)) {
                Intent(this@SplashActivity, QuizActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please make sure there is internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, delayMillis)
    }
}