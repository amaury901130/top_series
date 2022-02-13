package com.rr.android.network.repository.source

import com.rr.android.models.Episode
import com.rr.android.models.Show
import com.rr.android.network.connection.Data

interface SeriesRemoteDataSource {
    suspend fun browseByPage(page: Int): Result<Data<List<Show>>>
    suspend fun browseEpisodesByShow(showId: Int): Result<Data<List<Episode>>>
}
