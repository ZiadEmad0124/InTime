package com.ziad_emad_dev.in_time.network.project.project_members

import com.google.gson.annotations.SerializedName

data class GetProjectMembersResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("record")
    val record: List<MemberRecord>? = null
)