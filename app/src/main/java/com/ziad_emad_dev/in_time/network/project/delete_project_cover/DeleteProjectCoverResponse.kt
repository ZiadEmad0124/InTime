package com.ziad_emad_dev.in_time.network.project.delete_project_cover

import com.google.gson.annotations.SerializedName

data class DeleteProjectCoverResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("updatedProject")
    val updatedProject: UpdatedProject,
)
