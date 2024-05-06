package com.ziad_emad_dev.in_time.network.user

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: Int,

    @SerializedName("points")
    val points: Points
)