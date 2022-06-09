package com.chesia.bangkitcapstoneproject.Networking

import com.chesia.bangkitcapstoneproject.Networking.Maplist.MapListResponse
import com.chesia.bangkitcapstoneproject.Networking.Newslist.NewsListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList


interface ApiService {
    @FormUrlEncoded
    @POST("sign-up")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone:String,
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
        @Field("googleToken") googleToken: String
    ): Call<LoginResponse>

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
    ): Call<UserResponse>

    @GET("user/collection-point")
    fun getMapList(
        @Header("Authorization") token: String
    ): Call<MapListResponse>

    @GET("user/news")
    fun getNewsList(
        @Header("Authorization") token: String
    ): Call<NewsListResponse>

    @GET("user/trash-report")
    fun getHistory(
        @Header("Authorization") token: String
    ): Call<GetTrashResponse>

}