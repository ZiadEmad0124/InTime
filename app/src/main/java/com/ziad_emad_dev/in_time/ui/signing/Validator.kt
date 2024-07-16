package com.ziad_emad_dev.in_time.ui.signing

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.method.PasswordTransformationMethod
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.TimeUnit

class Validator(private val listener: ValidationListener) {

    fun nameEmptyError(name: String): Boolean {
        if (name.isEmpty()) {
            listener.onNameEmptyError()
        }
        return name.isEmpty()
    }

    fun emailEmptyError(email: String): Boolean {
        if (email.isEmpty()) {
            listener.onEmailEmptyError()
        }
        return email.isEmpty()
    }

    fun phoneEmptyError(phone: String): Boolean {
        if (phone.isEmpty()) {
            listener.onPhoneEmptyError()
        }
        return phone.isEmpty()
    }

    fun passwordEmptyError(password: String): Boolean {
        if (password.isEmpty()) {
            listener.onPasswordEmptyError()
        }
        return password.isEmpty()
    }

    fun confirmPasswordEmptyError(confirmPassword: String): Boolean {
        if (confirmPassword.isEmpty()) {
            listener.onConfirmPasswordEmptyError()
        }
        return confirmPassword.isEmpty()
    }

    val millisInFuture = 5 * 60 * 1000 // 5 minutes in milliseconds
    val countDownInterval = 1000 // 1 second in milliseconds

    private val countDownTimer = object : CountDownTimer(millisInFuture.toLong(), countDownInterval.toLong()) {
        @SuppressLint("DefaultLocale")
        override fun onTick(millisUntilFinished: Long) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)
            listener.onOTPTimer(String.format("in %02d:%02d", minutes, seconds))
        }

        override fun onFinish() {
            listener.onOTPTimer("Now")
        }
    }

    fun startCountDownTimer() {
        countDownTimer.start()
    }

    fun resetOTPTimer() {
        startCountDownTimer()
        listener.onResetOTPTimer()
    }

    fun otpCodeEmptyError(otpCode: String): Boolean {
        if (otpCode.isEmpty() || otpCode.length < 4) {
            listener.onOTPCodeEmptyError()
        }
        return otpCode.isEmpty() || otpCode.length < 4
    }

    fun nameValidationError(name: String): Boolean {
        if (name.length < 3) {
            listener.onNameValidationError()
        }
        return name.length < 3
    }

    fun emailValidationError(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            listener.onEmailValidationError()
        }
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun phoneValidationError(phone: String): Boolean {
        if (phone.length < 11) {
            listener.onPhoneValidationError()
        }
        return phone.length < 11
    }

    private fun validatePassword(password: String, errorCallback: (String) -> Unit): Boolean {
        if (password.length < 8) {
            errorCallback("Password must be at least 8 characters")
            return true
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            errorCallback("Password must contain at least one lowercase letter")
            return true
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            errorCallback("Password must contain at least one uppercase letter")
            return true
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            errorCallback("Password must contain at least one digit")
            return true
        } else if (!password.matches(Regex(".*[!@#\$%^&*+(,)/\"':?-].*"))) {
            errorCallback("Password must contain at least one special character")
            return true
        }
        return false
    }

    fun passwordValidationError(password: String): Boolean {
        return validatePassword(password) { errorMessage ->
            listener.onPasswordValidationError(errorMessage)
        }
    }

    fun confirmPasswordValidationError(confirmPassword: String): Boolean {
        return validatePassword(confirmPassword) { errorMessage ->
            listener.onConfirmPasswordValidationError(errorMessage)
        }
    }

    fun passwordsMatchError(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            listener.onPasswordsMatchError()
        }
        return password != confirmPassword
    }

    fun passwordToggle(passwordInputLayout: TextInputLayout) {
        passwordInputLayout.setEndIconOnClickListener {
            val selection = passwordInputLayout.editText?.selectionEnd
            val hasPasswordTransformation = passwordInputLayout.editText?.transformationMethod is PasswordTransformationMethod
            listener.onPasswordToggle(hasPasswordTransformation)
            if (selection != null) {
                passwordInputLayout.editText?.setSelection(selection)
            }
        }
    }

    fun confirmPasswordToggle(confirmPasswordInputLayout: TextInputLayout) {
        confirmPasswordInputLayout.setEndIconOnClickListener {
            val selection = confirmPasswordInputLayout.editText?.selectionEnd
            val hasPasswordTransformation = confirmPasswordInputLayout.editText?.transformationMethod is PasswordTransformationMethod
            listener.onConfirmPasswordToggle(hasPasswordTransformation)
            if (selection != null) {
                confirmPasswordInputLayout.editText?.setSelection(selection)
            }
        }
    }

    fun nameFocusChangeListener(nameInputLayout: TextInputLayout) {
        nameInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val message = if (hasFocus) {
                null
            } else {
                val name = nameInputLayout.editText?.text.toString().trim()
                if (name.isEmpty()) {
                    "Name is empty"
                } else if (name.length < 3) {
                    "Name must be at least 3 characters"
                } else {
                    null
                }
            }
            listener.onNameFocusChange(hasFocus, message)
        }
    }

    fun emailFocusChangeListener(emailInputLayout: TextInputLayout) {
        emailInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val message = if (hasFocus) {
                null
            } else {
                val email = emailInputLayout.editText?.text.toString().trim()
                if (email.isEmpty()) {
                    "Email is empty"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    "Email is invalid"
                } else {
                    null
                }
            }
            listener.onEmailFocusChange(hasFocus, message)
        }
    }

    fun phoneFocusChangeListener(phoneInputLayout: TextInputLayout) {
        phoneInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val message = if (hasFocus) {
                null
            } else {
                val phone = phoneInputLayout.editText?.text.toString().trim()
                if (phone.isEmpty()) {
                    "Phone is empty"
                } else if (phone.length < 11) {
                    "Phone must be 10 characters"
                } else {
                    null
                }
            }
            listener.onPhoneFocusChange(hasFocus, message)
        }
    }

    private fun validatePassword(password: String): String? {
        if (password.isEmpty()) {
            return "Password is empty"
        } else if (password.length < 8) {
            return "Password must be at least 8 characters"
        } else if (!password.matches(Regex(".*[a-z].*"))) {
            return "Password must contain at least one lowercase letter"
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            return "Password must contain at least one uppercase letter"
        } else if (!password.matches(Regex(".*[0-9].*"))) {
            return "Password must contain at least one digit"
        } else if (!password.matches(Regex(".*[!@#\$%^&*+(,)/\"':?-].*"))) {
            return "Password must contain at least one special character"
        }
        return null
    }

    fun passwordFocusChangeListener(passwordInputLayout: TextInputLayout) {
        passwordInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val message = if (!hasFocus) {
                val password = passwordInputLayout.editText?.text.toString().trim()
                validatePassword(password)
            } else {
                null
            }
            listener.onPasswordFocusChange(hasFocus, message)
        }
    }

    fun confirmPasswordFocusChangeListener(confirmPasswordInputLayout: TextInputLayout) {
        confirmPasswordInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            val message = if (!hasFocus) {
                val confirmPassword = confirmPasswordInputLayout.editText?.text.toString().trim()
                validatePassword(confirmPassword)
            } else {
                null
            }
            listener.onConfirmPasswordFocusChange(hasFocus, message)
        }
    }

    fun clearFocusEditTextLayout() {
        listener.onClearFocusEditTextLayout()
    }

    fun startLoading() {
        listener.onStartLoading()
    }

    fun stopLoading() {
        listener.onStopLoading()
    }
}