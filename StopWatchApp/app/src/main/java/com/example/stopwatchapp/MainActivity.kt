package com.example.stopwatchapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.stopwatchapp.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isHold = false
    private var seconds = 0
    private var isRunning = false
    private var wasRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds")
            isRunning = savedInstanceState.getBoolean("isRunning")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }


        startTheStopWatch()

    }

    private fun startTheStopWatch() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val realSeconds = seconds % 60
                val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, realSeconds)
                binding.timerTV.text = time
                if (isRunning) {
                    seconds++
                }
                mainHandler.postDelayed(this, 1000)
            }
        })
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("seconds",seconds)
        outState.putBoolean("isRunning",isRunning)
        outState.putBoolean("wasRunning",wasRunning)
    }

    fun startTimer(view: View) {
        isRunning = true
    }

    fun holdTimer(view: View) {
        if (isHold) {
            wasRunning = isRunning
            isRunning = true
            binding.holdBtn.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.white)
            binding.holdBtn.setTextColor(ContextCompat.getColor(this, R.color.MaterialPrimaryColor))
            isHold = false
        } else {
            wasRunning = isRunning
            isRunning = false

            binding.holdBtn.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.MaterialPrimaryColor)
            binding.holdBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
            isHold = true
        }
    }

    fun stopTimer(view: View) {
       isRunning = false
        seconds = 0
    }
}