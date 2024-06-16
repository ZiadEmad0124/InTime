package com.ziad_emad_dev.in_time.network.tasks.create_task

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("name")
    val name: String,

    @SerializedName("disc")
    val disc: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("tag")
    val tag: String,

    @SerializedName("repeat")
    val repeat: Repeat,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("_id")
    val _id: String,

    @SerializedName("__v")
    val __v: Int
)