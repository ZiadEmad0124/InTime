package com.ziad_emad_dev.in_time.network.auth.resend_activation_code

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResendActivationCode {

    interface ResendActivationCodeCallback {
        fun onResult(message: String)
    }

    fun resendActivationCode(email: String, password: String, callback: ResendActivationCodeCallback) {

        val request = ResendActivationCodeRequest(email, password)

        InTimeApi.retrofitService.resendActivationCode(request).enqueue(object : Callback<ResendActivationCodeResponse> {
            override fun onFailure(call: Call<ResendActivationCodeResponse>, t: Throwable) {
                callback.onResult("Failed Connect, Try Again")
            }

            override fun onResponse(call: Call<ResendActivationCodeResponse>, response: Response<ResendActivationCodeResponse>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.message.toString())
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorActivationResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    callback.onResult(errorActivationResponse?.message.toString())
                }
            }
        })
    }
}