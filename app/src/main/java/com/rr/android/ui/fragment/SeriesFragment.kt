package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rr.android.databinding.FragmentSeriesBinding
import com.rr.android.ui.base.BaseFragment

class SeriesFragment : BaseFragment() {

    private val binding: FragmentSeriesBinding by lazy {
        FragmentSeriesBinding.inflate(this.layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


}
