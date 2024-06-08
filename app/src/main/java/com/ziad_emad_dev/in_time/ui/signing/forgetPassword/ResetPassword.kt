package com.ziad_emad_dev.in_time.ui.signing.forgetPassword

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentResetPasswordBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel
import java.util.concurrent.TimeUnit

class ResetPassword : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    val millisInFuture = 5 * 60 * 1000
    val countDownInterval = 1000

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
        passwordToggle(binding.passwordLayout, binding.password)
        passwordToggle(binding.confirmPasswordLayout, binding.confirmPassword)
        countDownTimer.start()
        clickOnNewPasswordButton()
        resendOTP()
        responseComing()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun focusOnEditTextLayout() {

        binding.otpCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {

                binding.otpCode.setLineColor(R.drawable.pin_line)
            }
        }
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

    private fun passwordToggle(passwordLayout: TextInputLayout, password: EditText) {
        passwordLayout.setEndIconOnClickListener {
            val selection = password.selectionEnd
            val hasPasswordTransformation = password.transformationMethod is PasswordTransformationMethod
            if (hasPasswordTransformation) {
                password.transformationMethod = null
            } else {
                password.transformationMethod = AsteriskPasswordTransformation()
            }
            password.setSelection(selection)
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
            val otpCode = binding.otpCode.text.toString()
            val email = requireArguments().getString("email").toString()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            clearFocusEditTextLayout()
            if (!otpCodeEmptyError(otpCode)) {
                if (!(passwordEmptyError(password) && confirmPasswordEmptyError(confirmPassword))) {
                    if (!(passwordValidationError(binding.passwordLayout, password) || passwordValidationError(binding.confirmPasswordLayout, confirmPassword))) {
                        if (!passwordsMatchError(password, confirmPassword)) {
                            startLoading()
                            viewModel.resetPassword(otpCode, email, password, confirmPassword)
                        }
                    }
                }
            }
        }
    }

    private fun otpCodeEmptyError(otpCode: String): Boolean {
        if (otpCode.isEmpty() || otpCode.length < 4) {

            binding.otpCode.setLineColor(Color.RED)
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
        if (password.length < 8) {
            passwordLayout.error = getString(R.string.password_must_be_at_least_8_characters)
            return true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_lowercase_letter)
            return true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_uppercase_letter)
            return true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_digit)
            return true
        } else if (!password.matches(Regex(".*[!@#\$_%^&*+(,)/\"\':?-].*"))) {
            passwordLayout.error = getString(R.string.password_must_contain_at_least_one_special_character)
            return true
        } else {
            return false
        }
    }

    private fun passwordsMatchError(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
        }
        return password != confirmPassword
    }

    private fun startLoading() {
        binding.blockingView.visibility = View.VISIBLE

        binding.otpCode.setLineColor(R.drawable.pin_line)

        binding.saveNewPasswordButton.setBackgroundResource(R.drawable.button_loading_background)
        binding.saveNewPasswordButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.saveNewPasswordButton.setBackgroundResource(R.drawable.button_background)
        binding.saveNewPasswordButton.text = getString(R.string.save_new_password)
        binding.blockingView.visibility = View.GONE
    }

    private fun clearFocusEditTextLayout() {
        binding.password.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    private fun passwordMustBeUnique() {
        binding.passwordLayout.error = getString(R.string.password_must_be_unique)
        binding.confirmPasswordLayout.error = getString(R.string.password_must_be_unique)
    }

    private fun responseComing() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkCodePasswordAndNetwork(message)
        }
    }

    private fun checkCodePasswordAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "Invalid OTP" -> {

                binding.otpCode.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
            }

            "true" -> {
                binding.otpCode.setLineColor(Color.GREEN)
                findNavController().navigate(R.id.action_resetPassword_to_signIn)
                Toast.makeText(requireContext(), "Password Changed Successfully, SignIn Now", Toast.LENGTH_SHORT).show()
            }

            "password must be unique" -> {
                passwordMustBeUnique()
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    Error
    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == getString(R.string.now)) {
                val otpCode = binding.otpCode.text.toString()
                val email = requireArguments().getString("email").toString()
                val password = binding.password.text.toString().trim()
                val confirmPassword = binding.confirmPassword.text.toString().trim()
                stopLoading()
                viewModel.resetPassword(otpCode, email, password, confirmPassword)
                countDownTimer.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}