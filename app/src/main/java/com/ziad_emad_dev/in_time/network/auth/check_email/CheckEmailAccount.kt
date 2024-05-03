package com.ziad_emad_dev.in_time.network.auth.check_email

import com.google.gson.Gson
import com.ziad_emad_dev.in_time.network.InTimeApi
import com.ziad_emad_dev.in_time.network.auth.sign_in.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckEmailAccount {

    interface CheckEmailCallback {
        fun onResult(message: String)
    }

    fun checkEmail(email: String, callback: CheckEmailCallback) {

        val request = CheckEmailRequest(email = email)

        InTimeApi.retrofitService.checkEmail(request).enqueue(object : Callback<CheckEmailResponse> {

            override fun onFailure(call: Call<CheckEmailResponse>, t: Throwable) {
                callback.onResult("Failed Connect, Try Again")
            }

            override fun onResponse(call: Call<CheckEmailResponse>, response: Response<CheckEmailResponse>) {
                if (response.isSuccessful) {
                    callback.onResult(response.body()?.success.toString())
                } else {
                    val errorResponse = response.errorBody()?.string()
                    val errorLogInResponse = Gson().fromJson(errorResponse, SignInResponse::class.java)
                    callback.onResult(errorLogInResponse.success.toString())
                }
            }
        })
    }
}