package com.ziad_emad_dev.in_time.ui.signing.forgetPassword

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.FragmentResetPasswordBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.AuthViewModel

class ResetPassword : Fragment(), ValidationListener {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var validator: Validator

    private val viewModel by lazy {
        AuthViewModel(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validator = Validator(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator.startCountDownTimer()

        validator.passwordToggle(binding.passwordLayout)
        validator.confirmPasswordToggle(binding.confirmPasswordLayout)

        validator.passwordFocusChangeListener(binding.passwordLayout)
        validator.confirmPasswordFocusChangeListener(binding.confirmPasswordLayout)

        clickOnNewPasswordButton()

        resendOTP()

        binding.password.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun clickOnNewPasswordButton() {
        binding.saveNewPasswordButton.setOnClickListener {
            val otpCode = binding.otpCode.text.toString()
            val email = requireArguments().getString("email").toString()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            validator.clearFocusEditTextLayout()
            if (!validator.otpCodeEmptyError(otpCode)) {
                if (!(validator.passwordEmptyError(password) && validator.confirmPasswordEmptyError(confirmPassword))) {
                    if (!(validator.passwordValidationError(password) || validator.passwordValidationError(confirmPassword))) {
                        if (!validator.passwordsMatchError(password, confirmPassword)) {
                            validator.startLoading()
                            viewModel.resetPassword(otpCode, email, password, confirmPassword)
                            waitingForResponse()
                        }
                    }
                }
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            checkCodePasswordAndNetwork(message)
        }
    }

    private fun checkCodePasswordAndNetwork(message: String) {
        validator.stopLoading()
        when (message) {
            "Invalid OTP" -> {
                binding.otpCode.setLineColor(Color.RED)
                Toast.makeText(requireContext(), "Code is Wrong, Try Again", Toast.LENGTH_SHORT).show()
            }

            "true" -> {
                binding.otpCode.setLineColor(Color.GREEN)
                Toast.makeText(requireContext(), "Password Changed Successfully, SignIn Now", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_resetPassword_to_signIn)
            }

            "password must be unique" -> {
                binding.passwordLayout.error = getString(R.string.password_must_be_unique)
                binding.confirmPasswordLayout.error = getString(R.string.password_must_be_unique)
            }

            else -> {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOTPTimer(timer: String) {
        _binding?.otpTimer?.text = timer
    }

    override fun onOTPCodeEmptyError() {
        binding.otpCode.setLineColor(Color.RED)
        Toast.makeText(requireContext(), "Please Enter The Code Contain 4 Digits", Toast.LENGTH_SHORT).show()
    }

    private fun resendOTP() {
        binding.otpTimer.setOnClickListener {
            if (binding.otpTimer.text == "Now") {
                validator.resetOTPTimer()
            }
        }
    }

    override fun onResetOTPTimer() {
        binding.saveNewPasswordButton.setOnClickListener {
            val otpCode = binding.otpCode.text.toString()
            val email = requireArguments().getString("email").toString()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            validator.startLoading()
            viewModel.resetPassword(otpCode, email, password, confirmPassword)
            waitingForResponse()
        }
    }

    override fun onPasswordEmptyError() {
        binding.passwordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onConfirmPasswordEmptyError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onPasswordValidationError(passwordError: String) {
        binding.passwordLayout.error = passwordError
    }

    override fun onConfirmPasswordValidationError(passwordError: String) {
        binding.confirmPasswordLayout.error = passwordError
    }

    override fun onPasswordsMatchError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
    }

    override fun onPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.password.transformationMethod = null
        } else {
            binding.password.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onConfirmPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.confirmPassword.transformationMethod = null
        } else {
            binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.passwordLayout.error = message
        } else {
            binding.passwordLayout.error = null
        }
    }

    override fun onConfirmPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.confirmPasswordLayout.error = message
        } else {
            binding.confirmPasswordLayout.error = null
        }
    }

    override fun onClearFocusEditTextLayout() {
        binding.otpCode.setLineColor(R.drawable.pin_line)
        binding.password.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    override fun onStartLoading() {
        binding.otpCode.setLineColor(R.drawable.pin_line)
        binding.blockingView.visibility = View.VISIBLE
        binding.saveNewPasswordButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.saveNewPasswordButton.text = getString(R.string.save_new_password)
        binding.blockingView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}