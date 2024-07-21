package com.ziad_emad_dev.in_time.network.auth.activate_account

import com.google.gson.annotations.SerializedName

data class ActivateAccountRequest(
    @SerializedName("email")
    val email: String
)