package com.ziad_emad_dev.in_time.network.auth.check_email

import com.google.gson.annotations.SerializedName

data class CheckEmailRequest(
    @SerializedName("email")
    val email: String
)