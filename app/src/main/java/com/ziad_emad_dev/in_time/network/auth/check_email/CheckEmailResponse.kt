package com.ziad_emad_dev.in_time.network.auth.check_email

import com.google.gson.annotations.SerializedName

data class CheckEmailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null
)
