package com.ziad_emad_dev.in_time.network.profile.user

import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("totalPoints")
    val totalPoints: Int,

    @SerializedName("daily")
    val daily: List<DailyPoint>,

    @SerializedName("monthly")
    val monthly: List<MonthlyPoints>,

    @SerializedName("yearly")
    val yearly: List<YearlyPoints>
)