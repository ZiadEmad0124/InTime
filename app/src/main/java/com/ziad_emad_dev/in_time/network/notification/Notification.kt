package com.ziad_emad_dev.in_time.network.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("message")
    val message: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("date")
    val date: String
)