package com.rr.android.network.repository.repo

import android.util.Log
import com.rr.android.models.Serie
import com.rr.android.network.repository.source.SeriesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {
    override fun getByPage(page: Int): Flow<List<Serie>> = flow {
        try {
            val response = remoteDataSource.browseByPage(page)
            emit(response.getOrNull()?.value ?: emptyList())
        } catch (ignore: Exception) {
            Log.e("API", ignore.toString())
            emit(emptyList())
        }
    }
}
