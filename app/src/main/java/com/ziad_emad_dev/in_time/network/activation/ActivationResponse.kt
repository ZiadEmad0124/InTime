package com.ziad_emad_dev.in_time.network.activation

import com.google.gson.annotations.SerializedName

data class ActivationResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String
)