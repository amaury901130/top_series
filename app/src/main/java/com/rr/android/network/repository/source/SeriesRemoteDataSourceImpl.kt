package com.rr.android.network.repository.source

import com.rr.android.models.Episode
import com.rr.android.models.Show
import com.rr.android.network.connection.Connection
import com.rr.android.network.connection.Data
import com.rr.android.network.services.SeriesService
import javax.inject.Inject

class SeriesRemoteDataSourceImpl @Inject constructor(
    private val actionCallback: Connection,
    private val seriesService: SeriesService
) : SeriesRemoteDataSource {
    override suspend fun browseByPage(page: Int): Result<Data<List<Show>>> {
        return actionCallback.call(
            seriesService.browseShowsByPage(page)
        )
    }

    override suspend fun browseEpisodesByShow(showId: Int): Result<Data<List<Episode>>> {
        return actionCallback.call(
            seriesService.browseEpisodes(showId)
        )
    }
}
