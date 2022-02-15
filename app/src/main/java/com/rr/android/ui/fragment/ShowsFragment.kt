package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rr.android.R
import com.rr.android.databinding.FragmentSeriesBinding
import com.rr.android.models.Show
import com.rr.android.ui.adapter.ShowsAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.SeriesVM
import com.rr.android.ui.viewmodel.ShowsVMStates
import com.rr.android.util.extensions.runAfterInactivityPeriod
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            lifecycleScope.launch {
                showsVM.vmState.collect { state -> onVmChangeState(state) }
            }
        }
        setUpBrowse()
        setUpMenuActions()
        super.onStarted()
    }

    private fun setUpMenuActions() {
        with(binding) {
            btnSettings.setOnClickListener { navigateTo(R.id.nav_to_settings) }
            btnPeople.setOnClickListener { navigateTo(R.id.nav_people_screen) }
        }
    }

    private fun setUpBrowse() {
        with(binding.searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.root.runAfterInactivityPeriod({ browseQuery(query) }, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    binding.root.runAfterInactivityPeriod(
                        { browseQuery(newText) },
                        MIN_TIME_BEFORE_QUERY
                    )
                    return true
                }
            })

            setOnCloseListener {
                with(showsVM) {
                    showsAdapter.submitList(null)
                    clean()
                    browseAllShows()
                }
                true
            }
        }
    }

    private fun browseQuery(query: String?) {
        query?.let {
            if (it.length >= MIN_QUERY_SIZE) {
                showsAdapter.submitList(null)
                showsVM.browseBy(it.trim())
            }
        }
    }

    private fun onVmChangeState(state: ShowsVMStates) {
        when (state) {
            ShowsVMStates.ITEMS_LOADED -> {
                if (showsVM.shows.isNotEmpty()) {
                    binding.emptyState.isVisible = false
                    showsAdapter.submitList(showsVM.shows)
                } else {
                    binding.emptyState.isVisible = true
                    simpleToast(R.string.no_browse_result)
                }
            }
            ShowsVMStates.ERROR -> showError(showsVM.error)
            ShowsVMStates.LOADING -> binding.browseProgress.isVisible = true
            ShowsVMStates.IDLE -> binding.browseProgress.isVisible = false
        }
    }

    override fun onSelect(show: Show) {
        showsVM.selectShow(show)
        navigateTo(R.id.nav_to_show_details)
    }

    companion object {
        private const val LIST_SPAN_COUNT = 3
        private const val MIN_QUERY_SIZE = 5
        private const val MIN_TIME_BEFORE_QUERY = 500L
    }
}
