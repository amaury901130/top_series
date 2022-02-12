package com.rr.android.network.repository.repo

import com.rr.android.models.Serie
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun getByPage(page: Int): Flow<List<Serie>>
}
