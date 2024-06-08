package com.ziad_emad_dev.in_time.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.activation.ActivationRequest
import com.ziad_emad_dev.in_time.network.auth.activation.ActivationResponse
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailRequest
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailResponse
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordRequest
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordResponse
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeRequest
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeResponse
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInRequest
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpRequest
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message get() = _message

    private val _accessToken = MutableLiveData<String>()
    val accessToken get() = _accessToken

    private val _refreshToken = MutableLiveData<String>()
    val refreshToken get() = _refreshToken

    fun signIn(email: String, password: String) {
        val request = SignInRequest(email = email, password = password)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.signIn(request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.success.toString()
                    _accessToken.value = response.body()?.accessToken.toString()
                    _refreshToken.value = response.body()?.refreshToken.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    _message.value = errorSignInResponse?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = "Failed Connect, Try Again"
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
                _message.value = "Failed Connect, Try Again"
            }
        }
    }

    fun activateAccount(code: String, email: String) {
        val request = ActivationRequest(email = email)

        viewModelScope.launch {
            try {
                val response = InTimeApi.retrofitService.activateAccount(code = code, request)
                if (response.isSuccessful) {
                    _message.value = response.body()?.message.toString()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorActivationRequest = Gson().fromJson(errorResponse, ActivationResponse::class.java)
                    _message.value = errorActivationRequest?.message.toString()
                }
            } catch (e: Exception) {
                _message.value = "Failed Connect, Try Again"
            }
        }
    }

    fun resendActivationCode(email: String, password: String) {
        val request = ResendActivationCodeRequest(email = email, password = password)

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
                _message.value = "Failed Connect, Try Again"
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
                _message.value = "Failed Connect, Try Again"
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
                _message.value = "Failed Connect, Try Again"
            }
        }
    }
}