package com.rr.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.rr.android.R
import com.rr.android.databinding.LayoutViewHolderSeasonBinding
import com.rr.android.models.Season
import com.rr.android.ui.base.BaseViewHolder

class SeasonsAdapter(val actions: EpisodesAdapter.Actions?) :
    ListAdapter<Season, BaseViewHolder<Season>>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Season> {
        val binding = LayoutViewHolderSeasonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Season>, position: Int) {
        holder.bindTo(
            getItem(position)
        )
    }

    inner class SeriesViewHolder(private val binding: LayoutViewHolderSeasonBinding) :
        BaseViewHolder<Season>(binding) {
        override fun bindTo(item: Season) {
            with(binding) {
                tvTitle.text = root.context.getString(R.string.season, item.name.toString())
                rvEpisodes.apply {
                    layoutManager = GridLayoutManager(root.context, GRID_SPAN)
                    adapter = EpisodesAdapter(actions).also {
                        it.submitList(item.episodes)
                    }
                }
            }
        }
    }

    companion object {
        const val GRID_SPAN = 2
        val diffCallBack: DiffUtil.ItemCallback<Season> =
            object : DiffUtil.ItemCallback<Season>() {
                override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean {
                    return areItemsTheSame(oldItem, newItem)
                }
            }
    }
}
