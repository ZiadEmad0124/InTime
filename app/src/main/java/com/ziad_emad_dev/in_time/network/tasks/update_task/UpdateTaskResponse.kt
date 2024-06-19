package com.ziad_emad_dev.in_time.network.tasks.update_task

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.tasks.Task

data class UpdateTaskResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: Task? = null
)