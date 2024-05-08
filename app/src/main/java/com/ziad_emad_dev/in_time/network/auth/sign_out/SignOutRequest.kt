package com.ziad_emad_dev.in_time.network.auth.sign_out

import com.google.gson.annotations.SerializedName

data class SignOutRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)