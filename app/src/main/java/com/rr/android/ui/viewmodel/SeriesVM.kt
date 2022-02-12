package com.rr.android.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rr.android.models.Serie
import com.rr.android.network.repository.repo.SeriesRepository
import com.rr.android.ui.base.BaseViewModel
import com.rr.android.util.NetworkState
import com.rr.android.util.STRING_EMPTY
import com.rr.android.util.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SeriesVM @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private var page: Int = INITIAL_PAGE
    private var currentQuery: String = INITIAL_QUERY

    private val _paginatedBrowse =
        MutableStateFlow(BrowseSeriesState.Success(emptyList()) as BrowseSeriesState)
    val paginatedBrowse: StateFlow<BrowseSeriesState> = _paginatedBrowse

    private val _browseFlow: Flow<List<Serie>>
        get() = seriesRepository.getByPage(page++)

    init {
        browseSeries()
    }

    private fun browseBy(query: String = STRING_EMPTY) {
        currentQuery = query
        if (currentQuery.isEmpty()) {
            browseSeries()
        } else {
            // TODO: browse by query
        }
    }

    private fun browseSeries() {
        viewModelScope.launch {
            _networkState.value = NetworkState.loading
            withContext(dispatcher.io) {
                _browseFlow.collect {
                    with(_paginatedBrowse) {
                        if (it.isNotEmpty()) {
                            emit(BrowseSeriesState.Success(it))
                        } else {
                            emit(BrowseSeriesState.Error(""))
                        }
                    }
                }
            }
            _networkState.value = NetworkState.idle
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_QUERY = STRING_EMPTY
    }
}

sealed class BrowseSeriesState {
    data class Success(val series: List<Serie>) : BrowseSeriesState()
    data class Error(val exception: String) : BrowseSeriesState()
}
