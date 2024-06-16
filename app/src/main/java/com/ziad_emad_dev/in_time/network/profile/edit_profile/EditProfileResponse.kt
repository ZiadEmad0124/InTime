package com.ziad_emad_dev.in_time.network.profile.edit_profile

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null
)