package com.rr.android.network.repository.repo

import com.rr.android.models.Season
import com.rr.android.models.Show
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun getShowsByPage(page: Int): Flow<List<Show>>
    fun getEpisodesByShow(showId: Int): Flow<List<Season>>
    fun getShowsByQuery(query: String): Flow<List<Show>>
}
