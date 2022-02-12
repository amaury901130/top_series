package com.rr.android.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.rr.android.databinding.FragmentSeriesBinding
import com.rr.android.ui.adapter.SeriesAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.BrowseSeriesState
import com.rr.android.ui.viewmodel.SeriesVM
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SeriesFragment : BaseFragment() {

    private val binding: FragmentSeriesBinding by lazy {
        FragmentSeriesBinding.inflate(this.layoutInflater)
    }

    private val seriesVM: SeriesVM by activityViewModels()

    private val seriesAdapter: SeriesAdapter by lazy { SeriesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    private fun setUpView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeNetwork(seriesVM)
                with(binding.rvSeries) {
                    layoutManager = GridLayoutManager(requireContext(), LIST_SPAN_COUNT)
                    adapter = seriesAdapter
                }
                seriesVM.paginatedBrowse.collect {
                    when (it) {
                        is BrowseSeriesState.Success -> seriesAdapter.submitList(it.series)
                        is BrowseSeriesState.Error -> showError(it.exception)
                    }
                }
            }
        }
    }

    companion object {
        private const val LIST_SPAN_COUNT = 3
    }
}
