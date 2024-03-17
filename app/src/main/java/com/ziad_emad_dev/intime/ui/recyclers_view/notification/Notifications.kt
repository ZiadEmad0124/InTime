package com.ziad_emad_dev.intime.ui.recyclers_view.notification

import com.ziad_emad_dev.intime.R

class Notifications {
    companion object {
        fun getNotifications(): List<Notification> {
            return listOf(
                Notification(
                    R.drawable.ic_done,
                    "You finished your first task",
                    "1/2/2023"
                ),
                Notification(
                    R.drawable.ic_error,
                    "You haven't finished your first task yet",
                    "15/1/2023"
                ),
                Notification(
                    R.drawable.ic_info,
                    "You task will finish soon",
                    "1/12/2023"
                ),
                Notification(
                    R.drawable.ic_done,
                    "You finished your first task",
                    "10/9/2023"
                ),
                Notification(
                    R.drawable.ic_error,
                    "You haven't finished your first task yet",
                    "5/1/2023"
                ),
                Notification(
                    R.drawable.ic_info,
                    "You task will finish soon",
                    "15/7/2023"
                )
            )
        }
    }
}