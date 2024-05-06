package com.ziad_emad_dev.in_time.network.auth.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)