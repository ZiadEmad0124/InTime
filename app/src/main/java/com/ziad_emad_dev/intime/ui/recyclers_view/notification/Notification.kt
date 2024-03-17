package com.ziad_emad_dev.intime.ui.recyclers_view.notification

import androidx.annotation.DrawableRes

data class Notification(
    @DrawableRes val icon: Int,
    val content: String,
    val time: String
)
