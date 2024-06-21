package com.ziad_emad_dev.in_time.network.project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(
    @SerializedName("name")
    val name: String,

    @SerializedName("members")
    val members: List<Member>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("_id")
    val id: String,

    @SerializedName("__v")
    val v: Int
) : Parcelable