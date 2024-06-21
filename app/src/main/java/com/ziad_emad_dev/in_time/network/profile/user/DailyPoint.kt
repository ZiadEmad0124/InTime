package com.ziad_emad_dev.in_time.network.profile.user

import com.google.gson.annotations.SerializedName

data class DailyPoint(
    @SerializedName("date")
    val date: String,

    @SerializedName("value")
    val value: Int,

    @SerializedName("_id")
    val id: String
)