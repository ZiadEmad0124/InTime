package com.ziad_emad_dev.in_time.network.profile.user

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: Int,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("points")
    val points: Points,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("about")
    val about: String? = null,

    @SerializedName("tasks")
    val tasks: Task
)