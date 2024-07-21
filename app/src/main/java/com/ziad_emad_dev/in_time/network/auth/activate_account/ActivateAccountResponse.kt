package com.ziad_emad_dev.in_time.network.auth.activate_account

import com.google.gson.annotations.SerializedName

data class ActivateAccountResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String
)