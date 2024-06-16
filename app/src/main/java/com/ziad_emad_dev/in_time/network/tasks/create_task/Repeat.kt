package com.ziad_emad_dev.in_time.network.tasks.create_task

import com.google.gson.annotations.SerializedName

data class Repeat(
    @SerializedName("isRepeated")
    val isRepeated: Boolean
)