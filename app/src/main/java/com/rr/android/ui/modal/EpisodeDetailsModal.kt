package com.rr.android.ui.modal

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rr.android.R
import com.rr.android.databinding.LayoutEpisodeDetailsBinding
import com.rr.android.models.Episode
import com.rr.android.util.extensions.loadUrl

class EpisodeDetailsModal(private val episode: Episode) : BottomSheetDialogFragment() {
    private val binding: LayoutEpisodeDetailsBinding by lazy {
        LayoutEpisodeDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        with(binding) {
            episode.image?.original?.let {
                cover.loadUrl(it)
            } ?: cover.setImageResource(R.mipmap.ic_launcher)
            tvTitle.text = episode.name
            tvSeason.text = getString(R.string.season, (episode.season ?: 0).toString())
            tvEpisode.text = getString(R.string.episode_no, (episode.number ?: 0).toString())
            tvSummary.text = Html.fromHtml(episode.summary)
            btnClose.setOnClickListener { dismiss() }
            btnPlay.setOnClickListener {
                Toast.makeText(
                    context,
                    R.string.not_implemented,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "EPISODE_DETAILS_MODAL"
        fun show(supportFragmentManager: FragmentManager, episode: Episode) {
            EpisodeDetailsModal(episode).show(supportFragmentManager, TAG)
        }
    }
}
