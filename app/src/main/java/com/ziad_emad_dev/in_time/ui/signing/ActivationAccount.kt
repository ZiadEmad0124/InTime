package com.ziad_emad_dev.in_time.ui.signing

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentActivationAccountBinding
import com.ziad_emad_dev.in_time.network.auth.activation.ActivateAccount
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCode
import java.util.concurrent.TimeUnit

class ActivationAccount : Fragment() {

    private var _binding: FragmentActivationAccountBinding? = null
    private val binding get() = _binding!!

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivationAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countDownTimer.start()
        clickOnNextButton()
        resendOTP()
    }

    private val countDownTimer = object : CountDownTimer(millisInFuture.toLong(), countDownInterval.toLong()) {
        @SuppressLint("DefaultLocale")
        override fun onTick(millisUntilFinished: Long) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)
            binding.otpTimer.text = String.format("in %02d:%02d", minutes, seconds)
        }

        override fun onFinish() {
            binding.otpTimer.text = getString(R.string.now)
        }
    }

    private fun clickOnNextButton() {
        binding.nextButton.setOnClickListener {
            val otpCode = binding.otpView.text.toString()
            if (!otpCodeEmptyError(otpCode)) {
                checkOTP(otpCode)
            }
        }
    }

    private fun otpCodeEmptyError(otpCode: String): Boolean {
        if (otpCode.isEmpty() || otpCode.length < 4) {
            binding.otpView.setLineColor(Color.RED)
            Toast.makeText(requireContext(), "Please Enter The Code Contain 4 Digits", Toast.LENGTH_SHORT).show()
        }
        return otpCode.isEmpty() || otpCode.length < 4
    }

    private fun checkOTP(otpCode: String) {
        val email = arguments?.getString("email").toString()
        ActivateAccount().activateAccount(email, otpCode, object : ActivateAccount.ActivationCodeCallback {
            override fun onResult(message: String) {
                checkCodeAndNetwork(message)
            }
        })
    }

    private fun checkCodeAndNetwork(message: String) {
        when (message) {
            "this account is now active" -> {
                binding.otpView.setLineColor(Color.GREEN)
                findNavController().navigate(R.id.action_activation_Account_to_signIn)
                Toast.makeText(requireContext(), "This account is active now, SignIn", Toast.LENGTH_SHORT).show()
            }

            "Failed Connect, Try Again" -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            else -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val email = arguments?.getString("email").toString()
                val password = arguments?.getString("password").toString()
                ResendActivationCode().resendActivationCode(email, password, object : ResendActivationCode.ResendActivationCodeCallback {
                    override fun onResult(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
                countDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}