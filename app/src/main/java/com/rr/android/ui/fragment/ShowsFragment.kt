package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.rr.android.R
import com.rr.android.databinding.FragmentSeriesBinding
import com.rr.android.models.Show
import com.rr.android.ui.adapter.ShowsAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.SeriesVM
import com.rr.android.ui.viewmodel.ShowsVMStates

class ShowsFragment : BaseFragment(), ShowsAdapter.Actions {

    private val binding: FragmentSeriesBinding by lazy {
        FragmentSeriesBinding.inflate(this.layoutInflater)
    }

    private val showsVM: SeriesVM by activityViewModels()

    private val showsAdapter: ShowsAdapter by lazy { ShowsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onStarted() {
        with(binding.rvSeries) {
            layoutManager = GridLayoutManager(requireContext(), LIST_SPAN_COUNT)
            adapter = showsAdapter
        }
        with(showsVM) {
            observeNetwork(this)
            showsVM.vmState.observe(viewLifecycleOwner, ::onVmChangeState)
        }
        super.onStarted()
    }

    private fun onVmChangeState(state: ShowsVMStates) {
        when (state) {
            ShowsVMStates.ITEMS_LOADED -> showsAdapter.submitList(showsVM.shows)
            ShowsVMStates.ERROR -> showError(showsVM.error)
            ShowsVMStates.IDLE -> {}
        }
    }

    override fun onSelect(show: Show) {
        showsVM.selectShow(show)
        navigateTo(R.id.nav_to_show_details)
    }

    companion object {
        private const val LIST_SPAN_COUNT = 3
    }
}
