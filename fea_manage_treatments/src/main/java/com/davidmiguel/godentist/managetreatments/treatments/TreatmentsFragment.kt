package com.davidmiguel.godentist.managetreatments.treatments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.core.utils.setupFadeThroughTransition
import com.davidmiguel.godentist.managetreatments.R
import com.davidmiguel.godentist.managetreatments.ViewModelFactory
import com.davidmiguel.godentist.managetreatments.databinding.FragmentTreatmentsBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class TreatmentsFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentTreatmentsBinding
    private val treatmentsViewModel: TreatmentsViewModel by viewModels { ViewModelFactory.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFadeThroughTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        DataBindingUtil.inflate<FragmentTreatmentsBinding>(
            inflater, R.layout.fragment_treatments, container, false
        ).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = treatmentsViewModel
            initContent()
            return root
        }
    }

    private fun initContent() {
        binding.treatmentsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TreatmentsAdapter(treatmentsViewModel)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showFAB(
            RC.drawable.ic_add_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            treatmentsViewModel.addNewTreatment()
        }
        treatmentsViewModel.addEditTreatmentEvent.observeEvent(viewLifecycleOwner) { treatmentId ->
            findNavController().navigate(
                TreatmentsFragmentDirections.actionTreatmentsFragmentToAddTreatmentFragment(
                    if (treatmentId.isBlank()) null else treatmentId
                )
            )
        }
    }

    override fun onResumeAuthenticated() {
        treatmentsViewModel.start()
    }
}
