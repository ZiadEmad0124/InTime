package com.ziad_emad_dev.in_time.network.project.project_members

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tasks(
    @SerializedName("completedTasks")
    val completedTasks: Int,

    @SerializedName("onGoingTasks")
    val onGoingTasks: Int
) : Parcelable