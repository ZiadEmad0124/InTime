package com.ziad_emad_dev.in_time.network

import com.ziad_emad_dev.in_time.network.auth.activation.ActivationRequest
import com.ziad_emad_dev.in_time.network.auth.activation.ActivationResponse
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailRequest
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailResponse
import com.ziad_emad_dev.in_time.network.auth.new_password.NewPasswordRequest
import com.ziad_emad_dev.in_time.network.auth.new_password.NewPasswordResponse
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeRequest
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeResponse
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInRequest
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpRequest
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://intime-9hga.onrender.com/api/v1/"

//  Auth Endpoints
private const val SIGNIN_URL = "auth/login"
private const val SIGNUP_URL = "auth/signup"
private const val ACTIVATION_CODE_URL = "auth/activation/{code}"
private const val RESEND_ACTIVATION_CODE_URL = "auth/resendactivationcode"
private const val CHECK_EMAIL_URL = "auth/forgetpassword"
private const val CHANGE_PASSWORD_URL = "auth/forgetpassword/changepassword/{code}"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface InTimeApiServices {

    @POST(SIGNIN_URL)
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST(SIGNUP_URL)
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST(ACTIVATION_CODE_URL)
    fun sendCode(@Path("code") code: String, @Body request: ActivationRequest): Call<ActivationResponse>

    @POST(RESEND_ACTIVATION_CODE_URL)
    fun resendActivationCode(@Body request: ResendActivationCodeRequest): Call<ResendActivationCodeResponse>

    @POST(CHECK_EMAIL_URL)
    fun checkEmail(@Body request: CheckEmailRequest): Call<CheckEmailResponse>

    @POST(CHANGE_PASSWORD_URL)
    fun newPassword(@Path("code") code: String, @Body request: NewPasswordRequest): Call<NewPasswordResponse>
}

object InTimeApi {
    val retrofitService: InTimeApiServices by lazy {
        retrofit.create(InTimeApiServices::class.java)
    }
}