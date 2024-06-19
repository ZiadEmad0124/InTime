package com.ziad_emad_dev.in_time.network.project.create_project

import com.google.gson.annotations.SerializedName

data class CreateProjectResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Record? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null,
)