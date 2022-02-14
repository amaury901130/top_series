package com.rr.android.network.repository.source

import com.rr.android.models.People
import com.rr.android.network.connection.Data

interface PeopleRemoteDataSource {
    suspend fun browseByQuery(query: String, page: Int): Result<Data<List<People>>>
}
