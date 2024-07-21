package com.ziad_emad_dev.in_time.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.SessionManager
import com.ziad_emad_dev.in_time.network.auth.activate_account.ActivateAccountRequest
import com.ziad_emad_dev.in_time.network.auth.activate_account.ActivateAccountResponse
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailRequest
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailResponse
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeRequest
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeResponse
import com.ziad_emad_dev.in_time.network.auth.reset_password.ResetPasswordRequest
import com.ziad_emad_dev.in_time.network.auth.reset_password.ResetPasswordResponse
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInRequest
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpRequest
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpResponse
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    companion object {
        private const val FAILED_CONNECT = "Failed Connect, Try Again"
    }

    private val sessionManager = SessionManager(context)

    private val _message = MutableLiveData<String>()
    val message get() = _message

    fun signIn(email: String, password: String) {
        val request = SignInRequest(email = email, password = password)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signIn(request)
                if (response.isSuccessful) {
                    sessionManager.saveAccessToken(response.body()?.accessToken.toString())
                    sessionManager.saveRefreshToken(response.body()?.refreshToken.toString())
                    _message.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    _message.value = errorSignInResponse?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }

    fun signUp(name: String, email: String, phone: String, password: String) {
        val request = SignUpRequest(name = name, email = email, phone = phone, password = password)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signUp(request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.message.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignUpResponse = Gson().fromJson(errorResponse, SignUpResponse::class.java)
                    _message.value = errorSignUpResponse?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }

    fun activateAccount(code: String, email: String) {
        val request = ActivateAccountRequest(email = email)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.activateAccount(code = code, request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorActivateAccountRequest = Gson().fromJson(errorResponse, ActivateAccountResponse::class.java)
                    _message.value = errorActivateAccountRequest?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }

    fun resendActivationCode(email: String) {
        val request = ResendActivationCodeRequest(email = email)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.resendActivationCode(request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.message.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorResendActivationCodeRequest = Gson().fromJson(errorResponse, ResendActivationCodeResponse::class.java)
                    _message.value = errorResendActivationCodeRequest?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }

    fun checkEmail(email: String) {
        val request = CheckEmailRequest(email = email)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.checkEmail(request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorCheckEmailRequest = Gson().fromJson(errorResponse, CheckEmailResponse::class.java)
                    _message.value = errorCheckEmailRequest.success.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }

    fun resetPassword(code: String, email: String, password: String, confirmPassword: String) {
        val request = ResetPasswordRequest(email = email, password = password, confirmPassword = confirmPassword)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.resetPassword(code = code, request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.success.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorResetPasswordRequest = Gson().fromJson(errorResponse, ResetPasswordResponse::class.java)
                    _message.value = errorResetPasswordRequest?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = FAILED_CONNECT
            }
        }
    }
}