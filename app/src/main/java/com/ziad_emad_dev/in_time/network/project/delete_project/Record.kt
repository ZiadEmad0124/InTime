package com.ziad_emad_dev.in_time.network.project.delete_project

import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("acknowledged")
    val acknowledged: Boolean,

    @SerializedName("deletedCount")
    val deletedCount: Int
)