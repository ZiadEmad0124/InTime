package com.ziad_emad_dev.in_time.network.project.delete_project_cover

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ziad_emad_dev.in_time.network.project.Project
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdatedProject(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("record")
    val record: Project,

    @SerializedName("status")
    val status: Int
) : Parcelable