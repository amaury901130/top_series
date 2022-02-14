package com.rr.android.network.services

import com.rr.android.models.Episode
import com.rr.android.models.People
import com.rr.android.models.Show
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("shows")
    fun browseShowsByPage(@Query("page") page: Int): Call<List<Show>>

    @GET("shows")
    fun browseShowsByQuery(@Query("q") query: String): Call<List<Show>>

    @GET("shows/{id}/episodes")
    fun browseEpisodes(@Path("id") showId: Int): Call<List<Episode>>

    @GET("people")
    fun browsePeople(@Query("q") query: String, @Query("page") page: Int): Call<List<People>>
}
