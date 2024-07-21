package com.ziad_emad_dev.in_time.network.auth.reset_password

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String
)