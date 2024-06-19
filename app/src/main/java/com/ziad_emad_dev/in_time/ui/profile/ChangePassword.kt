package com.ziad_emad_dev.in_time.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ziad_emad_dev.in_time.R
import com.ziad_emad_dev.in_time.databinding.ActivityChangePasswordBinding
import com.ziad_emad_dev.in_time.ui.signing.AsteriskPasswordTransformation
import com.ziad_emad_dev.in_time.viewmodels.ProfileViewModel

class ChangePassword : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private val viewModel by lazy {
        ProfileViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myToolbar()
        focusOnEditTextLayout()
        passwordToggle(binding.currentPassword, binding.currentPasswordLayout)
        passwordToggle(binding.newPassword, binding.newPasswordLayout)
        passwordToggle(binding.confirmPassword, binding.confirmPasswordLayout)
        clickOnSaveButton()
        responseComing()

        binding.currentPassword.transformationMethod = AsteriskPasswordTransformation()
        binding.newPassword.transformationMethod = AsteriskPasswordTransformation()
        binding.confirmPassword.transformationMethod = AsteriskPasswordTransformation()
    }

    private fun myToolbar() {
        binding.myToolbar.title.text = getString(R.string.change_password)
        binding.myToolbar.back.setOnClickListener {
            finish()
        }
    }

    private fun focusOnEditTextLayout() {
        binding.currentPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.currentPasswordLayout.error = null
            } else {
                if (!passwordEmptyError(binding.currentPassword.text.toString().trim(), binding.currentPasswordLayout)) {
                    passwordValidationError(binding.currentPassword.text.toString().trim(), binding.currentPasswordLayout)
                }
            }
        }
        binding.newPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.newPasswordLayout.error = null
            } else {
                if (!passwordEmptyError(binding.newPassword.text.toString().trim(), binding.newPasswordLayout)) {
                    passwordValidationError(binding.newPassword.text.toString().trim(), binding.newPasswordLayout)
                }
            }
        }
        binding.confirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.confirmPasswordLayout.error = null
            } else {
                if (!passwordEmptyError(binding.confirmPassword.text.toString().trim(), binding.confirmPasswordLayout)) {
                    passwordValidationError(binding.confirmPassword.text.toString().trim(), binding.confirmPasswordLayout)
                    passwordsMatchError(binding.newPassword.text.toString().trim(), binding.confirmPassword.text.toString().trim())
                }
            }
        }
    }

    private fun passwordToggle(password: TextInputEditText, passwordLayout: TextInputLayout) {
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

    private fun clickOnSaveButton() {
        binding.saveButton.setOnClickListener {
            val currentPassword = binding.currentPassword.text.toString().trim()
            val newPassword = binding.newPassword.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()
            clearFocusEditTextLayout()
            if (!(passwordEmptyError(currentPassword, binding.currentPasswordLayout) &&
                        passwordEmptyError(newPassword, binding.newPasswordLayout) &&
                        passwordEmptyError(confirmPassword, binding.confirmPasswordLayout))) {
                if (!(passwordValidationError(currentPassword, binding.currentPasswordLayout) ||
                            passwordValidationError(newPassword, binding.newPasswordLayout) ||
                            passwordValidationError(confirmPassword, binding.confirmPasswordLayout))) {
                    if (!passwordsMatchError(newPassword, confirmPassword)) {
                        startLoading()
                        viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                    }
                }
            }
        }
    }

    private fun passwordEmptyError(password: String, passwordLayout: TextInputLayout): Boolean {
        if (password.isEmpty()) {
            passwordLayout.error = getString(R.string.empty_field)
        }
        return password.isEmpty()
    }

    private fun passwordValidationError(password: String, passwordLayout: TextInputLayout): Boolean {
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
        binding.saveButton.setBackgroundResource(R.drawable.button_loading)
        binding.saveButton.text = null
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressCircular.visibility = View.GONE
        binding.saveButton.setBackgroundResource(R.drawable.button_background)
        binding.saveButton.text = getString(R.string.save)
        binding.blockingView.visibility = View.GONE
    }

    private fun clearFocusEditTextLayout() {
        binding.currentPassword.clearFocus()
        binding.newPassword.clearFocus()
        binding.confirmPassword.clearFocus()
    }

    private fun responseComing() {
        viewModel.changePasswordMessage.observe(this) { message ->
            checkAccountAndNetwork(message)
        }
    }

    private fun wrongPassword() {
        binding.currentPasswordLayout.error = getString(R.string.wrong_password)
    }

    private fun sameOldPassword() {
        binding.newPasswordLayout.error = getString(R.string.must_be_different_from_the_current_password)
        binding.confirmPasswordLayout.error = getString(R.string.must_be_different_from_the_current_password)
    }

    private fun checkAccountAndNetwork(message: String) {
        stopLoading()
        when (message) {
            "\"newPassword\" contains an invalid value" -> {
                sameOldPassword()
            }

            "wrong password" -> {
                wrongPassword()
            }

            "true" -> {
                binding.passwordChanged.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 700)
            }

            else -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}