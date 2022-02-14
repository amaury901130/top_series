package com.rr.android.network.repository.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun seriesRepo(seriesRepository: SeriesRepositoryImpl): SeriesRepository

    @Binds
    @Singleton
    abstract fun peopleRepo(peopleRepository: PeopleRepositoryImpl): PeopleRepository
}
