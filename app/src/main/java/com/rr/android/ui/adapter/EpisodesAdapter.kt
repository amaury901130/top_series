package com.rr.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rr.android.databinding.LayoutEpisodeViewHolderBinding
import com.rr.android.models.Episode
import com.rr.android.ui.base.BaseViewHolder

class EpisodesAdapter(val actions: Actions?) :
    ListAdapter<Episode, BaseViewHolder<Episode>>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Episode> {
        val binding = LayoutEpisodeViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Episode>, position: Int) {
        holder.bindTo(
            getItem(position)
        )
    }

    inner class SeriesViewHolder(private val binding: LayoutEpisodeViewHolderBinding) :
        BaseViewHolder<Episode>(binding) {
        override fun bindTo(item: Episode) {
            with(binding) {
                tvTitle.text = item.name
                item.image?.original?.let { Glide.with(root.context).load(it).into(cover) }
                content.setOnClickListener { actions?.onEpisodeSelect(item) }
            }
        }
    }

    interface Actions {
        fun onEpisodeSelect(episode: Episode)
    }

    companion object {
        val diffCallBack: DiffUtil.ItemCallback<Episode> =
            object : DiffUtil.ItemCallback<Episode>() {
                override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                    return areItemsTheSame(oldItem, newItem)
                }
            }
    }
}
