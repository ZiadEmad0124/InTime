package com.ziad_emad_dev.in_time.network.auth.sign_in

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInUser {

    interface SignInCallback {
        fun onResult(message: String, accessToken: String? = null, refreshToken: String? = null)
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {

        val request = SignInRequest(email = email, password = password)

        InTimeApi.retrofitService.signIn(request).enqueue(object : Callback<SignInResponse> {
            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                callback.onResult("Failed Connect, Try Again")
            }

            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                if (response.isSuccessful) {
                    val rightResponse = response.body()
                    val success = rightResponse?.success.toString()
                    val accessToken = rightResponse?.accessToken
                    val refreshToken = rightResponse?.refreshToken
                    callback.onResult(success, accessToken, refreshToken)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    callback.onResult(errorSignInResponse?.message.toString())
                }
            }
        })
    }
}