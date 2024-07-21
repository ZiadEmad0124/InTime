package com.ziad_emad_dev.in_time.network.auth.reset_password

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)