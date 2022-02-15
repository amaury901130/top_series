package com.rr.android.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rr.android.models.People
import com.rr.android.models.Show
import com.rr.android.network.repository.repo.PeopleRepository
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
class PeopleVM @Inject constructor(
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {

    var selectedPeople: People? = null

    private var page: Int = INITIAL_PAGE
    private var currentQuery: String = INITIAL_QUERY

    private val _currentState = MutableStateFlow(PeopleVMStates.IDLE)
    val vmState: StateFlow<PeopleVMStates>
        get() = _currentState

    var peoples = mutableListOf<People>()

    private val _browseFlow: Flow<List<People>>
        get() = peopleRepository.getPeoplesByName(currentQuery, page++)

    private val _browseShows: Flow<List<Show>>
        get() = peopleRepository.getPeopleShows(selectedPeople?.id ?: -1)

    fun browseByName(query: String) {
        currentQuery = query
        viewModelScope.launch {
            _currentState.emit(PeopleVMStates.LOADING)
            _browseFlow.collect {
                if (it.isNotEmpty()) {
                    peoples.clear()
                    peoples.addAll(it)
                    _currentState.emit(PeopleVMStates.ITEMS_LOADED)
                } else {
                    _currentState.emit(PeopleVMStates.ERROR)
                }
                idle()
            }
        }
    }

    private fun idle() {
        viewModelScope.launch { _currentState.emit(PeopleVMStates.IDLE) }
    }

    fun clean() {
        peoples.clear()
    }

    fun loadEpisodes() {
        viewModelScope.launch {
            _currentState.emit(PeopleVMStates.LOADING)
            _browseShows.collect {
                if (it.isNotEmpty()) {
                    selectedPeople?.shows = it
                    _currentState.emit(PeopleVMStates.EPISODES_LOADED)
                } else {
                    _currentState.emit(PeopleVMStates.ERROR)
                }
                idle()
            }
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_QUERY = STRING_EMPTY
    }
}

enum class PeopleVMStates {
    ITEMS_LOADED, ERROR, IDLE, LOADING, EPISODES_LOADED
}
