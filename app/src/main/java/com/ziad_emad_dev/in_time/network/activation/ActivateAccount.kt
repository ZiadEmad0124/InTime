package com.ziad_emad_dev.in_time.network.activation

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.sign_in.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivateAccount {

    interface ActivationCodeCallback {
        fun onResult(message: String)
    }

    fun activateAccount(email: String, activationCode: String, callback: ActivationCodeCallback) {

        val request = ActivationRequest(email)

        InTimeApi.retrofitService.sendCode(activationCode, request).enqueue(object : Callback<ActivationResponse> {
                override fun onFailure(call: Call<ActivationResponse>, t: Throwable) {
                    callback.onResult("Failed Connect, Try Again")
                }

                override fun onResponse(call: Call<ActivationResponse>, response: Response<ActivationResponse>) {
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