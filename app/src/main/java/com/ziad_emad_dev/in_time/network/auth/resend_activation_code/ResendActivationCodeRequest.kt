package com.ziad_emad_dev.in_time.network.auth.resend_activation_code

import com.google.gson.annotations.SerializedName

data class ResendActivationCodeRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)