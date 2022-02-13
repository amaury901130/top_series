package com.rr.android.network.repository.repo

import android.util.Log
import com.rr.android.models.Season
import com.rr.android.models.Show
import com.rr.android.network.repository.source.SeriesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {
    override fun getShowsByPage(page: Int): Flow<List<Show>> = flow {
        try {
            val response = remoteDataSource.browseByPage(page)
            emit(response.getOrNull()?.value ?: emptyList())
        } catch (ignore: Exception) {
            Log.e("API", ignore.toString())
            emit(emptyList())
        }
    }

    override fun getEpisodesByShow(showId: Int): Flow<List<Season>> = flow {
        try {
            val response = remoteDataSource.browseEpisodesByShow(showId)
            val seasons =
                response.getOrNull()?.value?.toMutableList()?.groupBy { it.season }?.map {
                    Season(
                        episodes = it.value,
                        name = it.value.first().season ?: 0
                    )
                }
            emit(seasons ?: emptyList())
        } catch (ignore: Exception) {
            Log.e("API", ignore.toString())
            emit(emptyList())
        }
    }
}
