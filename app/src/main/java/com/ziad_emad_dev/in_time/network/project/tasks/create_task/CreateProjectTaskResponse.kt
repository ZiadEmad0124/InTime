package com.ziad_emad_dev.in_time.network.project.tasks.create_task

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.tasks.Record

data class CreateProjectTaskResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: Record? = null,

    @SerializedName("status")
    val status: Int? = null
)
