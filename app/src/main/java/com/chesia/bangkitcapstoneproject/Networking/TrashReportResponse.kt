package com.chesia.bangkitcapstoneproject.Networking

import com.google.gson.annotations.SerializedName

data class TrashReportResponse(
    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)