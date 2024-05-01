package com.ziad_emad_dev.in_time.network.sign_in

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("accessToken")
    var accessToken: String? = null,

    @SerializedName("refreshToken")
    var refreshToken: String? = null,

    @SerializedName("message")
    var message: String? = null
)