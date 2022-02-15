package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rr.android.R
import com.rr.android.databinding.FragmentShowDetailBinding
import com.rr.android.models.Show
import com.rr.android.ui.adapter.ShowsAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.PeopleVM
import com.rr.android.ui.viewmodel.PeopleVMStates
import com.rr.android.ui.viewmodel.SeriesVM
import com.rr.android.util.extensions.loadUrl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PeopleDetailFragment : BaseFragment(), ShowsAdapter.Actions {

    private val binding: FragmentShowDetailBinding by lazy {
        FragmentShowDetailBinding.inflate(this.layoutInflater)
    }

    private val peopleVM: PeopleVM by activityViewModels()
    private val showsVM: SeriesVM by activityViewModels()

    private val seasonsAdapter: ShowsAdapter by lazy { ShowsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onStarted() {
        with(binding) {
            btnBack.setOnClickListener { requireActivity().onBackPressed() }
            peopleVM.selectedPeople?.let {
                it.image?.original?.let { image ->
                    cover.loadUrl(image)
                    coverTiny.loadUrl(image)
                }
                tvTitle.text = it.name
                episodes.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = seasonsAdapter
                }
                lifecycleScope.launch {
                    peopleVM.vmState.collect { state -> onVmChangeState(state) }
                }
                peopleVM.loadEpisodes()
            }
        }
        super.onStarted()
    }

    private fun onVmChangeState(state: PeopleVMStates) {
        when (state) {
            PeopleVMStates.EPISODES_LOADED -> {
                peopleVM.selectedPeople?.shows?.takeIf { it.isNotEmpty() }?.let {
                    seasonsAdapter.submitList(it)
                }
            }
            PeopleVMStates.LOADING -> binding.browseProgress.isVisible = true
            PeopleVMStates.ERROR -> showError()
            PeopleVMStates.IDLE -> binding.browseProgress.isVisible = false
        }
    }

    override fun onSelect(show: Show) {
        showsVM.selectShow(show)
        navigateTo(R.id.nav_to_show_details)
    }
}
