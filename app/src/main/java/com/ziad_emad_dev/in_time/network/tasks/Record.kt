package com.ziad_emad_dev.in_time.network.tasks

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("name")
    val name: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("startAt")
    val startAt: String,

    @SerializedName("endAt")
    val endAt: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("repeat")
    val repeat: Repeat,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("__v")
    val v: Int
)