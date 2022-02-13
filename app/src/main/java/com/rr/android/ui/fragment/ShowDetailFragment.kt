package com.rr.android.ui.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rr.android.R
import com.rr.android.databinding.FragmentShowDetailBinding
import com.rr.android.models.Episode
import com.rr.android.models.lifeTime
import com.rr.android.ui.adapter.EpisodesAdapter
import com.rr.android.ui.adapter.SeasonsAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.modal.EpisodeDetailsModal
import com.rr.android.ui.viewmodel.SeriesVM
import com.rr.android.ui.viewmodel.ShowsVMStates
import com.rr.android.util.STRING_EMPTY
import com.rr.android.util.extensions.loadUrl

class ShowDetailFragment : BaseFragment(), EpisodesAdapter.Actions {

    private val binding: FragmentShowDetailBinding by lazy {
        FragmentShowDetailBinding.inflate(this.layoutInflater)
    }

    private val showsVM: SeriesVM by activityViewModels()

    private val seasonsAdapter: SeasonsAdapter by lazy { SeasonsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onStarted() {
        with(binding) {
            showsVM.selectedShow?.let {
                it.image?.original?.let { image ->
                    cover.loadUrl(image)
                    coverTiny.loadUrl(image)
                }
                tvTitle.text = it.name
                rating.text = root.context.getString(
                    R.string.rating,
                    it.rating?.average?.toString() ?: DEFAULT_RATING.toString()
                )
                summary.text = Html.fromHtml(it.summary ?: STRING_EMPTY)
                genres.text = resources.getQuantityString(
                    R.plurals.genre,
                    it.genres?.size ?: 0,
                    it.genres?.joinToString(separator = ", ") ?: STRING_EMPTY
                )
                val lifeTime = it.lifeTime().toInt()
                onAir.text = resources.getQuantityString(
                    R.plurals.on_air,
                    lifeTime,
                    lifeTime
                )
                episodes.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = seasonsAdapter
                }
                showsVM.vmState.observe(viewLifecycleOwner, ::onVmChangeState)
                showsVM.loadEpisodes()
            }
        }
        super.onStarted()
    }

    private fun onVmChangeState(state: ShowsVMStates) {
        when (state) {
            ShowsVMStates.EPISODES_LOADED -> {
                showsVM.seasons.takeIf { it.isNotEmpty() }?.let {
                    seasonsAdapter.submitList(it)
                }
            }
            ShowsVMStates.LOADING -> binding.browseProgress.isVisible = true
            ShowsVMStates.ERROR -> showError(showsVM.error)
            ShowsVMStates.IDLE -> binding.browseProgress.isVisible = false
        }
    }

    companion object {
        const val DEFAULT_RATING = 1
    }

    override fun onEpisodeSelect(episode: Episode) {
        EpisodeDetailsModal.show(parentFragmentManager, episode)
    }
}
