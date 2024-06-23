package com.ziad_emad_dev.in_time.network.project.remove_member

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.Record

data class RemoveMemberResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("record")
    val record: Record
)