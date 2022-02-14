package com.rr.android.network.repository.repo

import com.rr.android.models.People
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {
    fun getPeoplesByName(name: String, page: Int): Flow<List<People>>
}
