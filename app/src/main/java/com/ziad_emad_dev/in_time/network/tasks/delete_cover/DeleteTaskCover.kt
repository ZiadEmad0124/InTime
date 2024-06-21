package com.ziad_emad_dev.in_time.network.tasks.delete_cover

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.tasks.Task

data class DeleteTaskCover(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: Task? = null
)