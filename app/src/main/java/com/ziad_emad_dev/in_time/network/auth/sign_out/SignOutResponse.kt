package com.ziad_emad_dev.in_time.network.auth.sign_out

import com.google.gson.annotations.SerializedName

data class SignOutResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("status")
    val status: Int
)