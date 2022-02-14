package com.rr.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.rr.android.databinding.LayoutSimpleDataViewHolderBinding
import com.rr.android.models.People
import com.rr.android.ui.base.BaseViewHolder

class PeopleAdapter(val actions: Actions?) :
    ListAdapter<People, BaseViewHolder<People>>(diffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<People> {
        val binding = LayoutSimpleDataViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<People>, position: Int) {
        holder.bindTo(
            getItem(position)
        )
    }

    inner class SeriesViewHolder(private val binding: LayoutSimpleDataViewHolderBinding) :
        BaseViewHolder<People>(binding) {
        override fun bindTo(item: People) {
            with(binding) {
                tvTitle.text = item.name
                item.image?.original?.let { Glide.with(root.context).load(it).into(cover) }
                content.setOnClickListener { actions?.onPeopleSelect(item) }
            }
        }
    }

    interface Actions {
        fun onPeopleSelect(people: People)
    }

    companion object {
        val diffCallBack: DiffUtil.ItemCallback<People> =
            object : DiffUtil.ItemCallback<People>() {
                override fun areItemsTheSame(oldItem: People, newItem: People): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: People, newItem: People): Boolean {
                    return areItemsTheSame(oldItem, newItem)
                }
            }
    }
}
