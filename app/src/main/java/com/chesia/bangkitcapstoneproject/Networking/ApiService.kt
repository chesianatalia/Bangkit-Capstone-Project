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
<<<<<<< Updated upstream
=======

    @Multipart
    @POST("user/trash-report")
    fun sendTrashReport(
        @Header("Authorization")token: String,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part photos: ArrayList<MultipartBody.Part?>,
        @Part("trashList") trashList: RequestBody?,
        @Part("point") point: RequestBody?,
        @Part("collectionPoint") collectionPoint: RequestBody?,
    ): Call<TrashReportResponse>

    @GET("me")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<UserProfileResponse>

    @GET("user/collection-point")
    fun getMapList(
        @Header("Authorization") token: String
    ): Call<MapListResponse>

    @GET("trash-report")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<GetTrashResponse>
>>>>>>> Stashed changes
}