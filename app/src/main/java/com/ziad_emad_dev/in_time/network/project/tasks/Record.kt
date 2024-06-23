package com.ziad_emad_dev.in_time.network.project.tasks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.tasks.Step
import com.ziad_emad_dev.in_time.network.tasks.Tag
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(

    @SerializedName("tag")
    val tag: Tag? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("disc")
    val disc: String? = null,

    @SerializedName("startAt")
    val startAt: String,

    @SerializedName("endAt")
    val endAt: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("repeat")
    val repeat: Repeat,

    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("projectId")
    val projectId: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("projectTask")
    val projectTask: Boolean,

    @SerializedName("backlog")
    val backlog: Boolean,

    @SerializedName("_id")
    val id: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("__v")
    val v: Int,

    @SerializedName("image")
    val image: String? = null,
) : Parcelable