package com.rr.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rr.android.databinding.LayoutSerieViewHolderBinding
import com.rr.android.models.Serie
import com.rr.android.ui.base.BaseViewHolder

class SeriesAdapter : ListAdapter<Serie, BaseViewHolder<Serie>>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Serie> {
        val binding = LayoutSerieViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Serie>, position: Int) {
        holder.bindTo(
            getItem(position)
        )
    }

    inner class SeriesViewHolder(private val binding: LayoutSerieViewHolderBinding) :
        BaseViewHolder<Serie>(binding) {
        override fun bindTo(item: Serie) {
            with(binding) {
                tvTitle.text = item.name
                item.image?.original?.let {
                    Glide.with(root.context).load(it).into(cover)
                }
            }
        }
    }

    companion object {
        val diffCallBack: DiffUtil.ItemCallback<Serie> = object : DiffUtil.ItemCallback<Serie>() {
            override fun areItemsTheSame(oldItem: Serie, newItem: Serie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Serie, newItem: Serie): Boolean {
                return areItemsTheSame(oldItem, newItem)
            }
        }
    }
}
