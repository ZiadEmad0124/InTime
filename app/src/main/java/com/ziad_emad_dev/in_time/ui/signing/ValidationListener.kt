package com.ziad_emad_dev.in_time.ui.signing

interface ValidationListener {

    fun onNameEmptyError() {}
    fun onEmailEmptyError() {}
    fun onPhoneEmptyError() {}
    fun onPasswordEmptyError() {}
    fun onConfirmPasswordEmptyError() {}

    fun onOTPTimer(timer: String) {}
    fun onResetOTPTimer() {}
    fun onOTPCodeEmptyError() {}

    fun onNameValidationError() {}
    fun onEmailValidationError() {}
    fun onPhoneValidationError() {}
    fun onPasswordValidationError(passwordError: String) {}
    fun onConfirmPasswordValidationError(passwordError: String) {}

    fun onPasswordsMatchError() {}

    fun onPasswordToggle(hasPasswordTransformation: Boolean) {}
    fun onConfirmPasswordToggle(hasPasswordTransformation: Boolean) {}

    fun onNameFocusChange(hasFocus: Boolean, message: String?) {}
    fun onEmailFocusChange(hasFocus: Boolean, message: String?) {}
    fun onPhoneFocusChange(hasFocus: Boolean, message: String?) {}
    fun onPasswordFocusChange(hasFocus: Boolean, message: String?) {}
    fun onConfirmPasswordFocusChange(hasFocus: Boolean, message: String?) {}

    fun onClearFocusEditTextLayout() {}

    fun onStartLoading() {}
    fun onStopLoading() {}
}