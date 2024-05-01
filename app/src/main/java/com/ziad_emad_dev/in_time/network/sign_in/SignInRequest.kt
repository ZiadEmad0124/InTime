package com.ziad_emad_dev.in_time.network.sign_in

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String
)
