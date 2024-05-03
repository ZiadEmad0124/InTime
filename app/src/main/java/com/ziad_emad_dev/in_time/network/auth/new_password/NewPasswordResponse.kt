package com.ziad_emad_dev.in_time.network.auth.new_password

import com.google.gson.annotations.SerializedName

data class NewPasswordResponse(
    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String
)