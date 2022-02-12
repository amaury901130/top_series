package com.rr.android.network.repository.repo

import com.rr.android.models.Serie
import com.rr.android.network.repository.source.SeriesRemoteDataSource
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {
    override suspend fun getByPage(page: Int): List<Serie> {
        val response = remoteDataSource.browseByPage(page)
        return response.getOrNull()?.value ?: emptyList()
    }
}
