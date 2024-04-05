package com.ziad_emad_dev.in_time.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityOnBoardingBinding
import com.ziad_emad_dev.in_time.ui.signing.SignPage

class OnBoarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.white2)

        clickOnGetStartedButton()
    }

    private fun clickOnGetStartedButton() {
        binding.getStartedButton.setOnClickListener {
            val intent = Intent(this, SignPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}