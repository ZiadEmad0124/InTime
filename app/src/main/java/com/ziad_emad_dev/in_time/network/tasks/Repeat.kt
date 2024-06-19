package com.ziad_emad_dev.in_time.network.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repeat(
    @SerializedName("isRepeated")
    val isRepeated: Boolean
) : Parcelable