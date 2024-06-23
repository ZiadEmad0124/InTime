package com.ziad_emad_dev.in_time.network.socket

data class ChatMessage(
    val senderName: String,
    val messageText: String,
    val senderImageResource: String
)