package com.chesia.bangkitcapstoneproject.Networking

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("data")
    val data: LoginResponseData? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResponseData(
    val token: String,
    val endpoint: String
)
