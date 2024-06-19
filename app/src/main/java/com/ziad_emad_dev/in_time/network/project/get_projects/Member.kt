package com.ziad_emad_dev.in_time.network.project.get_projects

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("memberId")
    val memberId: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("_id")
    val id: String
)