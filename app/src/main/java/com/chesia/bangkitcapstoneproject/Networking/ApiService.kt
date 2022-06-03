package com.chesia.bangkitcapstoneproject.Networking

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("status") status: String = "active"
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("sign-in")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("sign-in")
    fun loginwithgoogle(
        @Field("email") email: String,
        @Field("token") token: String
    ): Call<LoginResponse>

    @GET("me")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>
}