package com.ziad_emad_dev.in_time.network.tasks.get_tasks

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.tasks.Tag
import com.ziad_emad_dev.in_time.network.tasks.Task

data class GetTasksResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: List<Task>? = null,

    @SerializedName("tags")
    val tags: List<Tag>? = null,

    @SerializedName("message")
    val message: String? = null,
)