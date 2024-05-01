package com.ziad_emad_dev.in_time.network.sign_in

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInUser {

    interface SignInCallback {
        fun onResult(message: String)
    }

    fun signIn(email: String, password: String, callback: SignInCallback) {

        val request = SignInRequest(email = email, password = password)

        InTimeApi.retrofitService.signIn(request).enqueue(object : Callback<SignInResponse> {
            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                callback.onResult("Failed Connect, Try Again")
            }

            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.accessToken.toString())
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignInResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    callback.onResult(errorSignInResponse?.message.toString())
                }
            }
        })
    }
}