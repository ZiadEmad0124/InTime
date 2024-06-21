package com.ziad_emad_dev.in_time.network.project.project_members

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberRecord(
    @SerializedName("points")
    val points: Points,

    @SerializedName("tasks")
    val tasks: Tasks,

    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: Int,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("isActive")
    val isActive: Boolean,

    @SerializedName("role")
    val role: String,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("about")
    val about: String? = null,

    @SerializedName("__v")
    val version: Int
) : Parcelable