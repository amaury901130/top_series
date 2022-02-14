package com.rr.android.network.repository.source

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SeriesDataSourceModule {

    @Binds
    @Singleton
    abstract fun showsRemoteDataSource(seriesRemoteDataSource: SeriesRemoteDataSourceImpl): SeriesRemoteDataSource

    @Binds
    @Singleton
    abstract fun peopleRemoteDataSource(peopleRemoteDataSource: PeopleRemoteDataSourceImpl): PeopleRemoteDataSource
}
