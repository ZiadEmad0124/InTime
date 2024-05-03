package com.ziad_emad_dev.in_time.ui.forgetPassword

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentResetPasswordBinding
import com.ziad_emad_dev.in_time.network.auth.new_password.NewPasswordAccount
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

        focusOnEditTextLayout()
        countDownTimer.start()
        clickOnNewPasswordButton()
        resendOTP()
    }

    private fun focusOnEditTextLayout() {
        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordLayout.error = null
            } else {
                if (!passwordEmptyError(binding.password.text.toString().trim())) {
                    passwordValidationError(binding.passwordLayout, binding.password.text.toString().trim())
                }
            }
        }

        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.confirmPasswordLayout.error = null
            } else {
                if (!confirmPasswordEmptyError(binding.confirmPassword.text.toString().trim())) {
                    passwordValidationError(binding.confirmPasswordLayout, binding.confirmPassword.text.toString().trim())
                    passwordsMatchError(binding.password.text.toString().trim(), binding.confirmPassword.text.toString().trim())
                }
            }
        }
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

    private fun clickOnNewPasswordButton() {
        binding.saveNewPasswordButton.setOnClickListener {
            val otpCode = binding.otpView.text.toString()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            clearFocusEditTextLayout()
            if (!otpCodeEmptyError(otpCode)) {
                if (!(passwordEmptyError(password) && confirmPasswordEmptyError(confirmPassword))) {
                    if (!(passwordValidationError(binding.passwordLayout, password) || passwordValidationError(binding.confirmPasswordLayout, confirmPassword))) {
                        if (!passwordsMatchError(password, confirmPassword)) {
                            resetPassword(password, confirmPassword)
                        }
                    }
                }
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

    private fun passwordEmptyError(password: String): Boolean {
        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_field)
        }
        return password.isEmpty()
    }

    private fun confirmPasswordEmptyError(confirmPassword: String): Boolean {
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.error = getString(R.string.empty_field)
        }
        return confirmPassword.isEmpty()
    }

    private fun passwordValidationError(passwordLayout: TextInputLayout, password: String): Boolean {
        var isPasswordError = false
        if (password.length < 8) {
            passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            isPasswordError = true
        } else if (!password.matches(Regex(".*[!@#\$_%^&*+(,)/\"\':?-].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            isPasswordError = true
        }
        return isPasswordError
    }

    private fun passwordsMatchError(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
        }
        return password != confirmPassword
    }

    private fun clearFocusEditTextLayout() {
        binding.password.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    private fun resetPassword(password: String, confirmPassword: String) {
        val email = arguments?.getString("email").toString()
        val otpCode = binding.otpView.text.toString()
        NewPasswordAccount().newPassword(email, password, confirmPassword, otpCode, object : NewPasswordAccount.NewPasswordCallback {
            override fun onResult(message: String) {
                checkCodePasswordAndNetwork(message)
            }
        })
    }

    private fun passwordMustBeUnique() {
        binding.passwordLayout.error = getString(R.string.password_must_be_unique)
        binding.confirmPasswordLayout.error = getString(R.string.password_must_be_unique)
    }

    private fun checkCodePasswordAndNetwork(message: String) {
        when (message) {
            "Invalid Token" -> {
                binding.otpView.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
            }

            "password changed" -> {
//                Error
                binding.otpView.setLineColor(Color.GREEN)
                val intent = Intent(requireContext(), SignPage::class.java)
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(requireContext(), "Password Changed Successfully, SignIn", Toast.LENGTH_SHORT).show()
            }

            "password must be unique" -> {
                passwordMustBeUnique()
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val password = binding.password.text.toString().trim()
                val confirmPassword = binding.confirmPassword.text.toString().trim()
                resetPassword(password, confirmPassword)
                countDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}