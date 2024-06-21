package com.ziad_emad_dev.in_time.network.project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("members")
    val members: List<Member>,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("v")
    val v: Int,

    @SerializedName("photo")
    val photo: String? = null
) : Parcelable