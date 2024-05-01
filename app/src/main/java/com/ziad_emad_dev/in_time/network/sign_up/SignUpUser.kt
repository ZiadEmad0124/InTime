package com.ziad_emad_dev.in_time.network.sign_up

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpUser {

    interface SignUpCallback {
        fun onResult(message: String)
    }

    fun signUp(name: String, email: String, phone: String, password: String, callback: SignUpCallback) {

        val request = SignUpRequest(name = name, email = email, phone = phone, password = password)

        InTimeApi.retrofitService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback.onResult("Failed Connect, Try Again")
            }

            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.message.toString())
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorSignUpResponse = Gson().fromJson(errorResponse, SignUpResponse::class.java)
                    callback.onResult(errorSignUpResponse?.message.toString())
                }
            }
        })
    }
}