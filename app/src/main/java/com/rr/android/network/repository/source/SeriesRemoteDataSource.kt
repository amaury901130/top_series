package com.rr.android.network.repository.source

import com.rr.android.models.Serie
import com.rr.android.network.connection.Data

interface SeriesRemoteDataSource {
    suspend fun browseByPage(page: Int): Result<Data<List<Serie>>>
}
