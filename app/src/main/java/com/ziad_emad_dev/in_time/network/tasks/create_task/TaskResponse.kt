package com.ziad_emad_dev.in_time.network.tasks.create_task

import com.google.gson.annotations.SerializedName

data class TaskResponse(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Record? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null
)