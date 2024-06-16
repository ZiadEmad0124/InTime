package com.ziad_emad_dev.in_time.network.tasks.create_task

import com.google.gson.annotations.SerializedName
import java.io.File

data class TaskRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("disc")
    val disc: String,

    @SerializedName("tag")
    val tag: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("image")
    val image: File
)