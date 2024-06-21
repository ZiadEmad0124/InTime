package com.ziad_emad_dev.in_time.network.project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    @SerializedName("memberId")
    val memberId: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("_id")
    val id: String
) : Parcelable