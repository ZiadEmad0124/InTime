package com.ziad_emad_dev.in_time.network.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    @SerializedName("stepDisc")
    val stepDisc: String,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("_id")
    val id: String
) : Parcelable