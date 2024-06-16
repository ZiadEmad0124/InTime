package com.ziad_emad_dev.in_time.network.tasks.create_task

import com.google.gson.annotations.SerializedName

data class Step(
    @SerializedName("stepDisc")
    val stepDisc: String,

    @SerializedName("completed")
    val completed: Boolean? = null,

    @SerializedName("_id")
    val _id: String? = null,
)