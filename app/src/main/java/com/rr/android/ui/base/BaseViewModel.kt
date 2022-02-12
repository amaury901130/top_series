package com.rr.android.ui.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rr.android.util.NetworkState

open class BaseViewModel : ViewModel(), LifecycleObserver {
    var error: String? = null

    protected val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState
}
