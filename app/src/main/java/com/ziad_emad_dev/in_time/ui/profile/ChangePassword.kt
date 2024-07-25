package com.ziad_emad_dev.in_time.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityChangePasswordBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.ui.signing.ValidationListener
import com.ziad_emad_dev.in_time.ui.signing.Validator
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class ChangePassword : AppCompatActivity(), ValidationListener {

    private lateinit var binding: ActivityChangePasswordBinding

    private lateinit var validator: Validator

    private val viewModel by lazy {
        ProfileViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = Validator(this)

        appToolbar()

        validator.passwordToggle(binding.currentPasswordLayout)
        validator.newPasswordToggle(binding.newPasswordLayout)
        validator.confirmPasswordToggle(binding.confirmPasswordLayout)

        validator.passwordFocusChangeListener(binding.currentPasswordLayout)
        validator.newPasswordFocusChangeListener(binding.newPasswordLayout)
        validator.confirmPasswordFocusChangeListener(binding.confirmPasswordLayout)

        clickOnSaveButton()

        binding.currentPassword.transformationMethod = AsteriskPasswordTransformation()
        binding.newPassword.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun appToolbar() {
        binding.appToolbar.title.text = getString(R.string.change_password)
        binding.appToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun clickOnSaveButton() {
        binding.saveButton.setOnClickListener {
            val currentPassword = binding.currentPassword.text.toString().trim()
            val newPassword = binding.newPassword.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            validator.clearFocusEditTextLayout()
            if (!(validator.passwordEmptyError(currentPassword) &&
                        validator.newPasswordEmptyError(newPassword) &&
                        validator.confirmPasswordEmptyError(confirmPassword))
            ) {
                if (!(validator.passwordValidationError(currentPassword) ||
                            validator.newPasswordValidationError(newPassword) ||
                            validator.confirmPasswordValidationError(confirmPassword))
                ) {
                    if (!validator.passwordsMatchError(newPassword, confirmPassword)) {
                        validator.startLoading()
                        viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                        waitingForResponse()
                    }
                }
            }
        }
    }

    private fun waitingForResponse() {
        viewModel.changePasswordMessage.observe(this) { message ->
            checkAccountAndNetwork(message)
        }
    }

    private fun checkAccountAndNetwork(message: String) {
        validator.stopLoading()
        when (message) {
            "wrong password" -> {
                binding.currentPasswordLayout.error = getString(R.string.wrong_password)
            }

            "\"newPassword\" contains an invalid value" -> {
                binding.newPasswordLayout.error = getString(R.string.must_be_different_from_the_current_password)
                binding.confirmPasswordLayout.error = getString(R.string.must_be_different_from_the_current_password)
            }

            "true" -> {
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                finish()
            }

            else -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPasswordEmptyError() {
        binding.currentPasswordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onNewPasswordEmptyError() {
        binding.newPasswordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onConfirmPasswordEmptyError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_is_empty)
    }

    override fun onPasswordValidationError(passwordError: String) {
        binding.currentPasswordLayout.error = passwordError
    }

    override fun onNewPasswordValidationError(passwordError: String) {
        binding.newPasswordLayout.error = passwordError
    }

    override fun onConfirmPasswordValidationError(passwordError: String) {
        binding.confirmPasswordLayout.error = passwordError
    }

    override fun onPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.currentPassword.transformationMethod = null
        } else {
            binding.currentPassword.transformationMethod = AsteriskPasswordTransformation()
        }
    }

    override fun onNewPasswordToggle(hasPasswordTransformation: Boolean) {
        if (hasPasswordTransformation) {
            binding.newPassword.transformationMethod = null
        } else {
            binding.newPassword.transformationMethod = AsteriskPasswordTransformation()
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
            binding.currentPasswordLayout.error = message
        } else {
            binding.currentPasswordLayout.error = null
        }
    }

    override fun onNewPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.newPasswordLayout.error = message
        } else {
            binding.newPasswordLayout.error = null
        }
    }

    override fun onConfirmPasswordFocusChange(hasFocus: Boolean, message: String?) {
        if (!hasFocus) {
            binding.confirmPasswordLayout.error = message
        } else {
            binding.confirmPasswordLayout.error = null
        }
    }

    override fun onPasswordsMatchError() {
        binding.confirmPasswordLayout.error = getString(R.string.password_not_match)
    }

    override fun onClearFocusEditTextLayout() {
        binding.currentPassword.clearFocus()
        binding.newPassword.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    override fun onStartLoading() {
        binding.blockingView.root.visibility = View.VISIBLE
        binding.saveButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    override fun onStopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.saveButton.text = getString(R.string.save)
        binding.blockingView.root.visibility = View.GONE
    }
}