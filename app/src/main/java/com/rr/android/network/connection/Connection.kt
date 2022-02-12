package com.rr.android.network.connection


import retrofit2.Call

interface Connection {
    suspend fun <T> call(apiCall: Call<T>): Result<Data<T>>
}
