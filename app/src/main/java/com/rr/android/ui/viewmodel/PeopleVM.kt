package com.rr.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rr.android.models.People
import com.rr.android.network.repository.repo.PeopleRepository
import com.rr.android.ui.base.BaseViewModel
import com.rr.android.util.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleVM @Inject constructor(
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {

    private var page: Int = INITIAL_PAGE
    private var currentQuery: String = INITIAL_QUERY

    private val _currentState = MutableLiveData(PeopleVMStates.IDLE)
    val vmState: LiveData<PeopleVMStates>
        get() = _currentState

    var peoples = mutableListOf<People>()

    private val _browseFlow: Flow<List<People>>
        get() = peopleRepository.getPeoplesByName(currentQuery, page++)

    fun browseByName(query: String) {
        currentQuery = query
        viewModelScope.launch {
            _currentState.value = PeopleVMStates.LOADING
            _browseFlow.collect {
                if (it.isNotEmpty()) {
                    peoples.clear()
                    peoples.addAll(it)
                    _currentState.value = PeopleVMStates.ITEMS_LOADED
                } else {
                    _currentState.value = PeopleVMStates.ERROR
                }
            }
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_QUERY = STRING_EMPTY
    }
}

enum class PeopleVMStates {
    ITEMS_LOADED, ERROR, IDLE, LOADING
}
