package com.rr.android.network.services

import com.rr.android.models.Serie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesService {
    @GET("search/shows?page={page}")
    fun browseByPage(@Path("page") page: Int): Call<List<Serie>>
}
