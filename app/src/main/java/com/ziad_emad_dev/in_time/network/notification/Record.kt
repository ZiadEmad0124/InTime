package com.ziad_emad_dev.in_time.network.notification

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("notifications")
    val notifications: List<Notification>
)