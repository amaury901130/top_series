package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rr.android.databinding.FragmentSeriesBinding
import com.rr.android.models.People
import com.rr.android.ui.adapter.PeopleAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.PeopleVM
import com.rr.android.ui.viewmodel.PeopleVMStates

class PeopleSearchFragment : BaseFragment(), PeopleAdapter.Actions {

    private val peopleVM: PeopleVM by viewModels()

    private val peopleAdapter: PeopleAdapter by lazy {
        PeopleAdapter(this)
    }

    private val binding: FragmentSeriesBinding by lazy {
        FragmentSeriesBinding.inflate(this.layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onStarted() {
        peopleVM.vmState.observe(viewLifecycleOwner, ::onChangeState)
        super.onStarted()
    }

    private fun onChangeState(state: PeopleVMStates) {
        /*when (state) {
            PeopleVMStates.ITEMS_LOADED -> TODO()
            PeopleVMStates.ERROR -> TODO()
            PeopleVMStates.IDLE -> TODO()
            PeopleVMStates.LOADING -> TODO()
        }*/
    }

    override fun onPeopleSelect(people: People) {
        // TODO("Not yet implemented")
    }
}