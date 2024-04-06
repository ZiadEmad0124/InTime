package com.ziad_emad_dev.in_time.ui.project_tasks

import androidx.annotation.DrawableRes

data class ProjectTask(
    val taskType: String,
    val taskTypeColor: Int,
    @DrawableRes val taskUserImage: Int,
    val taskUser: String,
    val taskDate: String,
    val taskTitle: String,
    val isTaskMine: Boolean,
    val taskStatus: String
)
