package com.fpa.picsum.network

import com.fpa.picsum.model.PicSum
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServiceInterface {

    @GET("list")
    suspend fun getDataFromAPI(
        @Query("page") page: Int,
        @Query("limit") size: Int
    ): Response<List<PicSum>>

    @GET("list")
    suspend fun getDataList(
        @Query("page") page: Int,
        @Query("limit") size: Int
    ): List<PicSum>

    @GET("list")
    suspend fun getDataList1(
        @Query("page") page: Int,
        @Query("limit") size: Int
    ): Response<List<PicSum>>


}