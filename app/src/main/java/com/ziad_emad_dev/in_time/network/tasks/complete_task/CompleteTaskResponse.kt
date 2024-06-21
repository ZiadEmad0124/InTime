package com.ziad_emad_dev.in_time.network.tasks.complete_task

import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.tasks.User

data class CompleteTaskResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("User")
    val user: User? = null,

    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null
)