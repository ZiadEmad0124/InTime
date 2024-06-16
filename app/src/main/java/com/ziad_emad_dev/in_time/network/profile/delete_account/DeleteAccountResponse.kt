package com.ziad_emad_dev.in_time.network.profile.delete_account

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("messages")
    val messages: List<String>? = null,

    @SerializedName("message")
    val message: String? = null,
)
