package com.rr.android.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rr.android.models.Season
import com.rr.android.models.Show
import com.rr.android.network.repository.repo.SeriesRepository
import com.rr.android.ui.base.BaseViewModel
import com.rr.android.util.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val shows: MutableList<Show> = mutableListOf()

    private var _seasons: MutableList<Season> = mutableListOf()
    val seasons: List<Season>
        get() = _seasons

    private val _currentState = MutableStateFlow(ShowsVMStates.IDLE)
    val vmState: StateFlow<ShowsVMStates>
        get() = _currentState

    private val _browseFlow: Flow<List<Show>>
        get() = seriesRepository.getShowsByPage(page++)

    private val _browseEpisodes: Flow<List<Season>>?
        get() = selectedShow?.id?.let { return@let seriesRepository.getEpisodesByShow(it) }

    private val _browseByQuery: Flow<List<Show>>
        get() = seriesRepository.getShowsByQuery(currentQuery)

    init {
        browseAllShows()
    }

    fun browseBy(query: String = STRING_EMPTY) {
        if (currentQuery != query) {
            currentQuery = query
            page = INITIAL_PAGE
            shows.clear()
            if (currentQuery.isEmpty()) {
                browseAllShows()
            } else {
                browseQuery()
            }
        }
    }

    private fun browseQuery() {
        viewModelScope.launch {
            _currentState.emit(ShowsVMStates.LOADING)
            _browseByQuery.collect {
                if (it.isNotEmpty()) {
                    shows.addAll(it)
                    _currentState.emit(ShowsVMStates.ITEMS_LOADED)
                } else {
                    _currentState.emit(ShowsVMStates.ERROR)
                }
                idle()
            }
        }
    }

    private fun idle() {
        viewModelScope.launch { _currentState.emit(ShowsVMStates.IDLE) }
    }

    fun loadEpisodes() {
        _seasons.clear()
        viewModelScope.launch {
            _currentState.emit(ShowsVMStates.LOADING)
            _browseEpisodes?.collect {
                if (it.isNotEmpty()) {
                    _seasons.addAll(it)
                    _currentState.emit(ShowsVMStates.EPISODES_LOADED)
                } else {
                    _currentState.emit(ShowsVMStates.ERROR)
                }
                idle()
            }
        }
    }

    fun browseAllShows() {
        viewModelScope.launch {
            _currentState.emit(ShowsVMStates.LOADING)
            _browseFlow.collect {
                if (it.isNotEmpty()) {
                    shows.addAll(it)
                    _currentState.emit(ShowsVMStates.ITEMS_LOADED)
                } else {
                    _currentState.emit(ShowsVMStates.ERROR)
                }
                idle()
            }
        }
    }

    fun clean() {
        currentQuery = INITIAL_QUERY
        shows.clear()
        page = INITIAL_PAGE
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
    ITEMS_LOADED, ERROR, IDLE, EPISODES_LOADED, LOADING
}
