package com.ziad_emad_dev.in_time.network.profile.rank

import com.google.gson.annotations.SerializedName

data class RankResponse(
    @SerializedName("myRank")
    val myRank: Int
)
