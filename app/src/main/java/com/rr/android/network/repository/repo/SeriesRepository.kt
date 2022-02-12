package com.rr.android.network.repository.repo

import com.rr.android.network.repository.source.SeriesRemoteDataSource
import javax.inject.Inject

interface SeriesRepository {
    suspend fun getByPage(page: Int) : List<Any>
}