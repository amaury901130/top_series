package com.rr.android.network.repository.source

import com.rr.android.models.Serie
import com.rr.android.network.connection.Connection
import com.rr.android.network.connection.Data
import com.rr.android.network.services.SeriesService
import javax.inject.Inject

class SeriesRemoteDataSourceImpl @Inject constructor(
    private val actionCallback: Connection,
    private val seriesService: SeriesService
) : SeriesRemoteDataSource {
    override suspend fun browseByPage(page: Int): Result<Data<List<Serie>>> {
        return actionCallback.call(
            seriesService.browseByPage(page)
        )
    }
}
