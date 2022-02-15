package com.rr.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.rr.android.R
import com.rr.android.databinding.FragmentPeopleSearchBinding
import com.rr.android.models.People
import com.rr.android.ui.adapter.PeopleAdapter
import com.rr.android.ui.base.BaseFragment
import com.rr.android.ui.viewmodel.PeopleVM
import com.rr.android.ui.viewmodel.PeopleVMStates
import com.rr.android.util.extensions.runAfterInactivityPeriod
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PeopleSearchFragment : BaseFragment(), PeopleAdapter.Actions {

    private val peopleVM: PeopleVM by activityViewModels()

    private val peopleAdapter: PeopleAdapter by lazy {
        PeopleAdapter(this)
    }

    private val binding: FragmentPeopleSearchBinding by lazy {
        FragmentPeopleSearchBinding.inflate(this.layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onStarted() {
        with(binding) {
            btnBack.setOnClickListener { requireActivity().onBackPressed() }
            rvPeople.apply {
                layoutManager = GridLayoutManager(context, COLUMNS)
                adapter = peopleAdapter
            }
        }
        setUpBrowse()
        lifecycleScope.launch { peopleVM.vmState.collect { state -> onChangeState(state) } }
        super.onStarted()
    }

    private fun setUpBrowse() {
        with(binding.searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.root.runAfterInactivityPeriod({ browseQuery(query) }, 0)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    binding.root.runAfterInactivityPeriod(
                        { browseQuery(newText) },
                        MIN_TIME_BEFORE_QUERY
                    )
                    return true
                }
            })

            setOnCloseListener {
                peopleAdapter.submitList(null)
                peopleVM.clean()
                true
            }
        }
    }

    private fun browseQuery(query: String?) {
        query?.let {
            if (it.length >= MIN_QUERY_SIZE) {
                peopleAdapter.submitList(null)
                peopleVM.browseByName(it.trim())
            }
        }
    }

    private fun onChangeState(state: PeopleVMStates) {
        when (state) {
            PeopleVMStates.ITEMS_LOADED -> {
                peopleVM.peoples.takeIf { it.isNotEmpty() }?.let {
                    peopleAdapter.submitList(it)
                } ?: simpleToast(R.string.no_browse_result)
            }
            PeopleVMStates.ERROR -> showError()
            PeopleVMStates.IDLE -> binding.progress.isVisible = false
            PeopleVMStates.LOADING -> binding.progress.isVisible = true
        }
    }

    override fun onPeopleSelect(people: People) {
        peopleVM.selectedPeople = people
        navigateTo(R.id.nav_to_people_details)
    }

    companion object {
        const val MIN_QUERY_SIZE = 3
        const val COLUMNS = 3
        const val MIN_TIME_BEFORE_QUERY = 1000L
    }
}
