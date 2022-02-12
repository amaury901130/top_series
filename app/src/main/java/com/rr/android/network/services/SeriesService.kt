package com.rr.android.network.services

import com.rr.android.models.Serie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesService {
    @GET("shows")
    fun browseByPage(@Query("page") page: Int): Call<List<Serie>>
}
