package com.ziad_emad_dev.in_time.network.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    @SerializedName("tag")
    val tag: Tag? = null,

    @SerializedName("repeat")
    val repeat: Repeat,

    @SerializedName("projectTask")
    val projectTask: Boolean,

    @SerializedName("backlog")
    val backlog: Boolean,

    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("disc")
    val disc: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("image")
    val image: String?,

    @SerializedName("startAt")
    val startAt: String,

    @SerializedName("endAt")
    val endAt: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("__v")
    val v: Int
) : Parcelable