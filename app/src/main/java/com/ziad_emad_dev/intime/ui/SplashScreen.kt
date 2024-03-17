package com.ziad_emad_dev.intime.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.intime.databinding.ActivitySplashScreenBinding
import com.ziad_emad_dev.intime.ui.onboarding.OnBoarding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToOnBoardingPage()
    }

    private fun goToOnBoardingPage() {
        Handler(Looper.myLooper()!!).postDelayed(
            {
                val intent = Intent(this, OnBoarding::class.java)
                startActivity(intent)
                finish()
            },
            2000
        )
    }
}