package com.rr.android.network.repository.repo

import android.util.Log
import com.rr.android.models.People
import com.rr.android.network.repository.source.PeopleRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val remoteDataSource: PeopleRemoteDataSource
) : PeopleRepository {
    override fun getPeoplesByName(name: String, page: Int): Flow<List<People>> = flow {
        try {
            val response = remoteDataSource.browseByQuery(name, page)
            emit(response.getOrNull()?.value ?: emptyList())
        } catch (ignore: Exception) {
            Log.e("API", ignore.toString())
            emit(emptyList())
        }
    }
}
