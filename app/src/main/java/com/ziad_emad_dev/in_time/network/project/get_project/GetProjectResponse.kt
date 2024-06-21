package com.ziad_emad_dev.in_time.network.project.get_project

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.Project

data class GetProjectResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Project? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null,
)
