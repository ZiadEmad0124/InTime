package com.ziad_emad_dev.in_time.network.project.project_members

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Daily(
    @SerializedName("date")
    val date: String,

    @SerializedName("value")
    val value: Int,

    @SerializedName("_id")
    val id: String
) : Parcelable