package com.ziad_emad_dev.in_time.ui.activate_account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityActivateAccountBinding
import com.ziad_emad_dev.in_time.network.activation.ActivateAccount
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import java.util.concurrent.TimeUnit

class ActivateAccount : AppCompatActivity() {

    private lateinit var binding: ActivityActivateAccountBinding

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email").toString()

        checkOTP(email)
        countDownTimer.start()
        resendOTP()
    }

    private val countDownTimer = object : CountDownTimer(millisInFuture.toLong(), countDownInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)
                binding.otpTimer.text = String.format("in %02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.otpTimer.text = getString(R.string.now)
            }
        }

    private fun checkOTP(email: String) {
        binding.nextButton.setOnClickListener {
            if (!isOTPCodeEmpty()) {
                val otpCode = binding.otpView.text.toString()
                binding.otpView.setLineColor(getColor(R.color.secondary))
                ActivateAccount().activateAccount(email, otpCode, object : ActivateAccount.ActivationCodeCallback {
                    override fun onResult(message: String) {
                        checkCodeAndNetwork(message)
                    }
                })
            }
        }
    }

    private fun isOTPCodeEmpty(): Boolean {
        var isCodeEmpty = false
        if(binding.otpView.text.isNullOrEmpty()){
            binding.otpView.setLineColor(Color.RED)
            Toast.makeText(this, "Please Enter The Code", Toast.LENGTH_SHORT).show()
            isCodeEmpty = true
        }
        return isCodeEmpty
    }

    private fun checkCodeAndNetwork(message: String) {
        if (message == "this account is now active") {
            binding.otpView.setLineColor(Color.GREEN)
            Toast.makeText(this, "this account is active now, SignIn", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignPage::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.otpView.setLineColor(Color.RED)
            Toast.makeText(this, "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val email = intent.getStringExtra("email").toString()
                checkOTP(email)
                countDownTimer.start()
            }
        }
    }
}