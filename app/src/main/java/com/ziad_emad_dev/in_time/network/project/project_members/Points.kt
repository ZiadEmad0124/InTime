package com.ziad_emad_dev.in_time.network.project.project_members

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Points(
    @SerializedName("totalPoints")
    val totalPoints: Int,

    @SerializedName("daily")
    val daily: List<Daily>,

    @SerializedName("monthly")
    val monthly: List<Monthly>,

    @SerializedName("yearly")
    val yearly: List<Yearly>
) : Parcelable