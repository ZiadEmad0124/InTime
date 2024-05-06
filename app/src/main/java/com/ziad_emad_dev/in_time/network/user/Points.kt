package com.ziad_emad_dev.in_time.network.user

import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("daily")
    val daily: List<Int>,

    @SerializedName("monthly")
    val monthly: List<Int>,

    @SerializedName("yearly")
    val yearly: List<Int>
)