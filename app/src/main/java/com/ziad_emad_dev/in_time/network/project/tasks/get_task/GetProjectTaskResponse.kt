package com.ziad_emad_dev.in_time.network.project.tasks.get_task

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.tasks.Record

data class GetProjectTaskResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: List<Record>? = null,
)