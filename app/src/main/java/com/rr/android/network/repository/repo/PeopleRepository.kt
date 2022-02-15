package com.rr.android.network.repository.repo

import com.rr.android.models.People
import com.rr.android.models.Show
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {
    fun getPeoplesByName(name: String, page: Int): Flow<List<People>>
    fun getPeopleShows(id: Int): Flow<List<Show>>
}
