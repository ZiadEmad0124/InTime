package com.ziad_emad_dev.in_time.ui.forgetPassword

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentResetPasswordBinding
import com.ziad_emad_dev.in_time.network.new_password.NewPasswordAccount
import com.ziad_emad_dev.in_time.ui.signing.SignPage
import java.util.concurrent.TimeUnit

class ResetPassword : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email").toString()

        focusOnEditTextLayout()
        checkOTP(email)
        countDownTimer.start()
        resendOTP()
    }

    private fun focusOnEditTextLayout() {
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordLayout.error = null
            }
        }
        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.confirmPasswordLayout.error = null
            }
        }
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
        binding.saveNewPasswordButton.setOnClickListener {
            focusOnEditTextLayout()
            binding.otpView.setLineColor(resources.getColor(R.color.secondary, null))
            if (!(isOTPCodeEmpty() || arePasswordsEmpty())) {
                val otpCode = binding.otpView.text.toString()
                val password = binding.password.text.toString()
                val confirmPassword = binding.confirmPassword.text.toString()
                if (!(passwordValidationError(password) || passwordValidationError(confirmPassword))) {
                    if (!passwordsMatchError()) {
                        resetPassword(email, password, confirmPassword, otpCode)
                    }
                }
            }
        }
    }

    private fun isOTPCodeEmpty(): Boolean {
        var isCodeEmpty = false
        if (binding.otpView.text.isNullOrEmpty()) {
            binding.otpView.setLineColor(Color.RED)
            Toast.makeText(requireContext(), "Please Enter The Code", Toast.LENGTH_SHORT).show()
            isCodeEmpty = true
        }
        return isCodeEmpty
    }

    private fun arePasswordsEmpty(): Boolean {
        var isErrorFound = false
        if (binding.password.text?.trim().isNullOrEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        if (binding.confirmPassword.text?.trim().isNullOrEmpty()) {
            binding.confirmPasswordLayout.error = getString(R.string.empty_field)
            isErrorFound = true
        }
        return isErrorFound
    }

    private fun passwordValidationError(password: String): Boolean {
        var isPasswordError = false
        if (password.length < 8) {
            binding.passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[!@#\$%^&*].*"))) {
            binding.passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            isPasswordError = true
        } else {
            binding.passwordLayout.error = null
        }
        return isPasswordError
    }

    private fun passwordsMatchError(): Boolean {
        var isErrorFound = false
        if (binding.password.text.toString() != binding.confirmPassword.text.toString()) {
            binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
            isErrorFound = true
        }
        return isErrorFound
    }

    private fun resetPassword(email: String, password: String, confirmPassword: String, activationCode: String) {
        NewPasswordAccount().newPassword(email, password, confirmPassword, activationCode, object : NewPasswordAccount.NewPasswordCallback {
                override fun onResult(message: String) {
                    checkCodeAndNetwork(message)
                }
            })
    }

    private fun checkCodeAndNetwork(message: String) {
        when (message) {
            "Failed Connect, Try Again" -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            "false" -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT)
                    .show()
            }

            "true" -> {
                binding.otpView.setLineColor(Color.GREEN)
                Toast.makeText(requireContext(), "Password Changed Successfully, SignIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), SignPage::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val email = arguments?.getString("email").toString()
                checkOTP(email)
                countDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}