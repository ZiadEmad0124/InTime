package com.ziad_emad_dev.in_time.network.sign_up

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("password")
    var password: String,
)
