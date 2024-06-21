package com.ziad_emad_dev.in_time.network.project.share_project

import com.google.gson.annotations.SerializedName

data class ShareProjectResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("link")
    val link: String
)