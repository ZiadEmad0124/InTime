package com.ziad_emad_dev.in_time.network.new_password

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.sign_in.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPasswordAccount {

    interface NewPasswordCallback {
        fun onResult(message: String)
    }

    fun newPassword(email: String, password: String, confirmPassword: String, activationCode: String, callback: NewPasswordCallback) {

        val request = NewPasswordRequest(email, password, confirmPassword)

        InTimeApi.retrofitService.newPassword(activationCode, request).enqueue(object : Callback<NewPasswordResponse> {
                override fun onFailure(call: Call<NewPasswordResponse>, t: Throwable) {
                    callback.onResult("Failed Connect, Try Again")
                }

                override fun onResponse(call: Call<NewPasswordResponse>, response: Response<NewPasswordResponse>) {
                    if (response.isSuccessful) {
                        callback.onResult(response.body()?.success.toString())
                    } else {
                        val errorResponse = response.errorBody()?.string()
                        val errorActivationResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                        callback.onResult(errorActivationResponse?.success.toString())
                    }
                }
            })
    }
}