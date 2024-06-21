package com.ziad_emad_dev.in_time.network.project.delete_project

import com.google.gson.annotations.SerializedName

data class DeleteProjectResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Record? = null,

    @SerializedName("status")
    val status: Int? = null
)