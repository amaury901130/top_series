package com.rr.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rr.android.R
import com.rr.android.databinding.LayoutSerieViewHolderBinding
import com.rr.android.models.Show
import com.rr.android.ui.base.BaseViewHolder

class ShowsAdapter(val actions: Actions?) :
    ListAdapter<Show, BaseViewHolder<Show>>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Show> {
        val binding = LayoutSerieViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Show>, position: Int) {
        holder.bindTo(
            getItem(position)
        )
    }

    inner class SeriesViewHolder(private val binding: LayoutSerieViewHolderBinding) :
        BaseViewHolder<Show>(binding) {
        override fun bindTo(item: Show) {
            with(binding) {
                tvTitle.text = item.name
                item.image?.original?.let { Glide.with(root.context).load(it).into(cover) }
                rating.text = root.context.getString(
                    R.string.rating,
                    item.rating?.average?.toString() ?: DEFAULT_RATING.toString()
                )
                content.setOnClickListener { actions?.onSelect(item) }
            }
        }
    }

    interface Actions {
        fun onSelect(show: Show)
    }

    companion object {
        const val DEFAULT_RATING = 1
        val diffCallBack: DiffUtil.ItemCallback<Show> = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
                return areItemsTheSame(oldItem, newItem)
            }
        }
    }
}
