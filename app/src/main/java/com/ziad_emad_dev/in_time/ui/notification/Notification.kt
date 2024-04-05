package com.ziad_emad_dev.in_time.ui.notification

import androidx.annotation.DrawableRes

data class Notification(
    @DrawableRes val icon: Int,
    val content: String,
    val date: String
)
