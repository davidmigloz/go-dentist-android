package com.davidmiguel.godentist.manageworkdays.addworkday.addexectreatment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageworkdays.ViewModelFactory
import com.davidmiguel.godentist.manageworkdays.addworkday.AddWorkDayViewModel
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayExecTreatmentBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class AddWorkDayExecTreatmentFragment : AuthenticatedFragment() {

    private val args: AddWorkDayExecTreatmentFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddWorkDayExecTreatmentBinding
    private val addWorkDayViewModel: AddWorkDayViewModel
            by navGraphViewModels(RC.id.add_work_day_nav_graph) { ViewModelFactory.getInstance() }
    private val treatmentsAdapter
            by lazy { AddWorkDayExecTreatmentTreatmentsAdapter(requireContext()) }

    private var executedTreatmentId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        executedTreatmentId = args.executedTreatmentId
        FragmentAddWorkDayExecTreatmentBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addWorkDayViewModel
            initContent()
            return root
        }
    }

    private fun initContent() {
        binding.treatment.setAdapter(treatmentsAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        // Footer btn
        requireMainActivity().showFAB(
            RC.drawable.ic_done_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            addWorkDayViewModel.saveExecTreatment()
        }
        // Treatment
        addWorkDayViewModel.treatments.observe(viewLifecycleOwner, Observer { clinics ->
            treatmentsAdapter.setTreatments(clinics)
        })
        binding.treatment.setOnItemClickListener { _, _, position, _ ->
            addWorkDayViewModel.treatment.value = treatmentsAdapter.getItem(position)
        }
        addWorkDayViewModel.treatmentError.observe(viewLifecycleOwner, Observer { error ->
            binding.treatmentsContainer?.error = if (error) "Invalid treatment!" else null
        })
        // Price
        binding.price.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWorkDayViewModel.saveExecTreatment()
            }
            false
        }
        addWorkDayViewModel.priceError.observe(viewLifecycleOwner, Observer { error ->
            binding.priceContainer.error = if (error) "Invalid price!" else null
        })
        // Updated event
        addWorkDayViewModel.workDayExecTreatmentUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        // Snackbar
        addWorkDayViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    override fun onResumeAuthenticated() {
        addWorkDayViewModel.startAddWorkDayExecTreatment(executedTreatmentId)
    }
}
