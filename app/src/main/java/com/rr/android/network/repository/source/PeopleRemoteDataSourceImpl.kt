package com.rr.android.network.repository.source

import com.rr.android.models.People
import com.rr.android.network.connection.Connection
import com.rr.android.network.connection.Data
import com.rr.android.network.services.ApiService
import javax.inject.Inject

class PeopleRemoteDataSourceImpl @Inject constructor(
    private val actionCallback: Connection,
    private val seriesService: ApiService
) : PeopleRemoteDataSource {
    override suspend fun browseByQuery(query: String, page: Int): Result<Data<List<People>>> {
        return actionCallback.call(
            seriesService.browsePeople(query, page)
        )
    }
}
