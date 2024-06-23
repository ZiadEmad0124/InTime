package com.ziad_emad_dev.in_time.network.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Record? = null,

    @SerializedName("message")
    val message: String? = null
)