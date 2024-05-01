package com.ziad_emad_dev.in_time.network.activation

import com.google.gson.annotations.SerializedName

data class ActivationRequest(
    @SerializedName("email")
    val email: String
)