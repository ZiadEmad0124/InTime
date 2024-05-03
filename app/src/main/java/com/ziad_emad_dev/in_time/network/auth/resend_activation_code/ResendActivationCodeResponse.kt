package com.ziad_emad_dev.in_time.network.auth.resend_activation_code

import com.google.gson.annotations.SerializedName

data class ResendActivationCodeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)
