package com.ziad_emad_dev.in_time.network.tasks

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.profile.user.Points
import com.ziad_emad_dev.in_time.network.profile.user.Tasks

data class User(
    @SerializedName("points")
    val points: Points,

    @SerializedName("tasks")
    val tasks: Tasks,

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

    @SerializedName("isActive")
    val isActive: Boolean,

    @SerializedName("role")
    val role: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("__v")
    val v: Int
)