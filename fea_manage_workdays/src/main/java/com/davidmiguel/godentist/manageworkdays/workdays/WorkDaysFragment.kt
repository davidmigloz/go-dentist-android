package com.davidmiguel.godentist.manageworkdays.workdays

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageworkdays.ViewModelFactory
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentWorkDaysBinding
import com.davidmiguel.godentist.requireMainActivity
import com.davidmiguel.godentist.core.R as RC

class WorkDaysFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentWorkDaysBinding
    private val workDaysViewModel: WorkDaysViewModel by viewModels { ViewModelFactory.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentWorkDaysBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = workDaysViewModel
            initContent()
            return root
        }
    }

    private fun initContent() {
        binding.workDaysList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = WorkDaysAdapter()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showExtendedFAB(RC.drawable.ic_add_black_24dp, "Add workday") {
            workDaysViewModel.addNewWorkDay()
        }
        workDaysViewModel.addWorkDayEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(WorkDaysFragmentDirections.actionWorkDaysFragmentToAddWorkDayFragment())
        }
    }

    override fun onResumeAuthenticated() {
        workDaysViewModel.start()
    }
}
