package com.ziad_emad_dev.in_time.network.sign_up

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("success")
    var success: Boolean? = null,

    @SerializedName("message")
    var message: String
)
