package com.ziad_emad_dev.in_time.network.project.create_project

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("name")
    val name: String,

    @SerializedName("members")
    val members: List<Member>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("__v")
    val v: Int
)