package com.rr.android.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rr.android.R
import com.rr.android.util.LoadingDialogUtil
import com.rr.android.util.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseFragment : Fragment(), BaseView {

    init {
        start()
    }

    override fun showProgress() {
        LoadingDialogUtil.showProgress(requireContext())
    }

    override fun hideProgress() {
        LoadingDialogUtil.hideProgress()
    }

    override fun showError(message: String?) {
        LoadingDialogUtil.showError(message, requireContext())
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    private fun start() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { onStarted() }
        }
    }

    open fun onStarted() {
        // TODO: do something if needed
    }

    fun observeNetwork(baseViewModel: BaseViewModel) {
        baseViewModel.networkState.observe(this) {
            when (it) {
                NetworkState.loading -> showProgress()
                NetworkState.idle -> hideProgress()
                else -> showError(baseViewModel.error ?: getString(R.string.default_error))
            }
        }
    }

    fun navigateTo(routeOrAction: Int, bundle: Bundle? = null) {
        requireActivity().takeIf { it is BaseNavActivity }?.let {
            it as BaseNavActivity
            it.navigateTo(routeOrAction, bundle)
        }
    }

    fun simpleToast(message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
