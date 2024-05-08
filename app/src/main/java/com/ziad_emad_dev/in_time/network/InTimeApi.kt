package com.ziad_emad_dev.in_time.network

import com.ziad_emad_dev.in_time.network.auth.activation.ActivationRequest
import com.ziad_emad_dev.in_time.network.auth.activation.ActivationResponse
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailRequest
import com.ziad_emad_dev.in_time.network.auth.check_email.CheckEmailResponse
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordRequest
import com.ziad_emad_dev.in_time.network.auth.new_password.ResetPasswordResponse
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenRequest
import com.ziad_emad_dev.in_time.network.auth.refresh_token.RefreshTokenResponse
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeRequest
import com.ziad_emad_dev.in_time.network.auth.resend_activation_code.ResendActivationCodeResponse
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInRequest
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutRequest
import com.ziad_emad_dev.in_time.network.auth.sign_out.SignOutResponse
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpRequest
import com.ziad_emad_dev.in_time.network.auth.sign_up.SignUpResponse
import com.ziad_emad_dev.in_time.network.user.UserResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://intime-9hga.onrender.com/api/v1/"

//  Auth Endpoints
private const val SIGNIN_URL = "auth/login"
private const val SIGNUP_URL = "auth/signup"
private const val ACTIVATION_CODE_URL = "auth/activation/{code}"
private const val RESEND_ACTIVATION_CODE_URL = "auth/resendactivationcode"
private const val CHECK_EMAIL_URL = "auth/forgetpassword"
private const val CHANGE_PASSWORD_URL = "auth/forgetpassword/changepassword/{code}"
private const val REFRESH_TOKEN_URL = "auth/refreshToken"
private const val SIGNOUT_URL = "auth/signOut"

//  User Endpoints
private const val USER_URL = "user/"

private val okHttpClient = OkHttpClient.Builder()
    .readTimeout(300, TimeUnit.SECONDS)
    .connectTimeout(300, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface InTimeApiServices {

//  Auth

    @POST(SIGNIN_URL)
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @POST(SIGNUP_URL)
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST(ACTIVATION_CODE_URL)
    suspend fun activateAccount(@Path("code") code: String, @Body request: ActivationRequest): Response<ActivationResponse>

    @POST(RESEND_ACTIVATION_CODE_URL)
    suspend fun resendActivationCode(@Body request: ResendActivationCodeRequest): Response<ResendActivationCodeResponse>

    @POST(CHECK_EMAIL_URL)
    suspend fun checkEmail(@Body request: CheckEmailRequest): Response<CheckEmailResponse>

    @POST(CHANGE_PASSWORD_URL)
    suspend fun resetPassword(@Path("code") code: String, @Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @POST(REFRESH_TOKEN_URL)
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST(SIGNOUT_URL)
    suspend fun signOut(@Body request: SignOutRequest): Response<SignOutResponse>

//  User

    @GET(USER_URL)
    suspend fun fetchUser(@Header("Authorization") token: String): Response<UserResponse>
}

object InTimeApi {
    val retrofitService: InTimeApiServices by lazy {
        retrofit.create(InTimeApiServices::class.java)
    }
}