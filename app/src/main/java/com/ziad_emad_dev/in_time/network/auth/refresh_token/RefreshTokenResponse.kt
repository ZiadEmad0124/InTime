package com.ziad_emad_dev.in_time.network.auth.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("newAccessToken")
    val newAccessToken: String,

    @SerializedName("newRefreshToken")
    val newRefreshToken: String,

    @SerializedName("message")
    val message: String? = null
)