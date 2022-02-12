package com.rr.android.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rr.android.R
import com.rr.android.util.LoadingDialogUtil
import com.rr.android.util.NetworkState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment(), BaseView {

    override fun showProgress() {
        LoadingDialogUtil.showProgress(requireContext())
    }

    override fun hideProgress() {
        LoadingDialogUtil.hideProgress()
    }

    override fun showError(message: String?) {
        LoadingDialogUtil.showError(message, requireContext())
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
}
