package com.ziad_emad_dev.in_time.network.profile.remove_avatar

import com.google.gson.annotations.SerializedName

data class RemoveAvatarResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)