package com.ziad_emad_dev.in_time.network.project.tasks.remove_task

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.tasks.Record

data class DeleteProjectTaskResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: Record,

    @SerializedName("status")
    val status: Int
)