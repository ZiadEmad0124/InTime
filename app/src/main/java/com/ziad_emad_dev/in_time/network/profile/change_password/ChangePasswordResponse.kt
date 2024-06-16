package com.ziad_emad_dev.in_time.network.profile.change_password

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)