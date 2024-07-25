package com.ziad_emad_dev.in_time.network.profile

data class Profile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val avatar: String,
    val title: String,
    val about: String,
    val rank: String,
    val level: String,
    val totalPoints: String,
    val completedTasks: String,
    val onGoingTasks: String
)