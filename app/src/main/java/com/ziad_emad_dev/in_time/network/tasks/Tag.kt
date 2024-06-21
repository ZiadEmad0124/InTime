package com.ziad_emad_dev.in_time.network.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    @SerializedName("name")
    val name: String,

    @SerializedName("color")
    var color: String
) : Parcelable