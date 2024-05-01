package com.ziad_emad_dev.in_time.ui.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.databinding.ActivitySplashScreenBinding
import com.ziad_emad_dev.in_time.network.SessionManager
import com.ziad_emad_dev.in_time.ui.home.HomePage
import com.ziad_emad_dev.in_time.ui.on_boarding.OnBoarding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        startApp()
    }

    private fun startApp() {
        Handler(Looper.myLooper()!!).postDelayed(
            {
                if (sessionManager.isAuthEmpty()) {
                    val intent = Intent(this, OnBoarding::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2000
        )
    }
}