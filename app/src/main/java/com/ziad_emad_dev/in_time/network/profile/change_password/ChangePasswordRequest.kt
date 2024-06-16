package com.ziad_emad_dev.in_time.network.profile.change_password

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("oldPassword")
    val oldPassword: String,

    @SerializedName("newPassword")
    val newPassword: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)