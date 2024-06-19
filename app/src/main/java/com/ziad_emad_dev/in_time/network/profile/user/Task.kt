package com.ziad_emad_dev.in_time.network.profile.user

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("completedTasks")
    val completedTasks: Int,

    @SerializedName("onGoingTasks")
    val onGoingTasks: Int
)