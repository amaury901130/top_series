package com.rr.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rr.android.models.Season
import com.rr.android.models.Show
import com.rr.android.network.repository.repo.SeriesRepository
import com.rr.android.ui.base.BaseViewModel
import com.rr.android.util.NetworkState
import com.rr.android.util.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesVM @Inject constructor(
    private val seriesRepository: SeriesRepository
) : BaseViewModel() {

    private var page: Int = INITIAL_PAGE
    private var currentQuery: String = INITIAL_QUERY

    private var _selectedShow: Show? = null
    val selectedShow: Show?
        get() = _selectedShow

    private var _newItems: MutableList<Show> = mutableListOf()
    val shows: List<Show>
        get() = _newItems

    private var _seasons: MutableList<Season> = mutableListOf()
    val seasons: List<Season>
        get() = _seasons

    private val _currentState = MutableLiveData(ShowsVMStates.IDLE)
    val vmState: LiveData<ShowsVMStates>
        get() = _currentState

    private val _browseFlow: Flow<List<Show>>
        get() = seriesRepository.getShowsByPage(page++)

    private val _browseEpisodes: Flow<List<Season>>?
        get() = selectedShow?.id?.let { return@let seriesRepository.getEpisodesByShow(it) }

    init {
        browseSeries()
    }

    private fun browseBy(query: String = STRING_EMPTY) {
        if (currentQuery != query) {
            currentQuery = query
            page = INITIAL_PAGE
            if (currentQuery.isEmpty()) {
                _newItems.clear()
                browseSeries()
            } else {
                // TODO: browse by query
            }
        }
    }

    fun loadEpisodes() {
        _seasons.clear()
        viewModelScope.launch {
            _browseEpisodes?.collect {
                if (it.isNotEmpty()) {
                    _seasons.addAll(it)
                    _currentState.value = ShowsVMStates.EPISODES_LOADED
                } else {
                    _currentState.value = ShowsVMStates.ERROR
                }
            }
        }
    }

    private fun browseSeries() {
        viewModelScope.launch {
            _networkState.value = NetworkState.loading
            _browseFlow.collect {
                if (it.isNotEmpty()) {
                    _newItems.addAll(it)
                    _currentState.value = ShowsVMStates.ITEMS_LOADED
                } else {
                    _currentState.value = ShowsVMStates.ERROR
                }
                _networkState.value = NetworkState.idle
            }
        }
    }

    fun selectShow(show: Show) {
        _selectedShow = show
    }

    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_QUERY = STRING_EMPTY
    }
}

enum class ShowsVMStates {
    ITEMS_LOADED, ERROR, IDLE, EPISODES_LOADED
}
