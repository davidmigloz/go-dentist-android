package com.davidmiguel.godentist.manageworkdays.addworkdayexectreatment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageworkdays.ViewModelFactory
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayExecTreatmentBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class AddWorkDayExecTreatmentFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddWorkDayExecTreatmentBinding
    private val addWorkDayExecTreatmentViewModel: AddWorkDayExecTreatmentViewModel
            by viewModels { ViewModelFactory.getInstance() }
    private val treatmentsAdapter
            by lazy { AddWorkDayExecTreatmentTreatmentsAdapter(requireContext()) }

    private var workDayId: String? = null
    private var executedTreatmentId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentAddWorkDayExecTreatmentBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addWorkDayExecTreatmentViewModel
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
            addWorkDayExecTreatmentViewModel.saveTreatment()
        }
        // Treatment
        addWorkDayExecTreatmentViewModel.treatments.observe(viewLifecycleOwner, Observer { clinics ->
            treatmentsAdapter.setTreatments(clinics)
        })
        binding.treatment.setOnItemClickListener { _, _, position, _ ->
            addWorkDayExecTreatmentViewModel.treatment.value = treatmentsAdapter.getItem(position)
        }
        addWorkDayExecTreatmentViewModel.treatmentsError.observe(viewLifecycleOwner, Observer { error ->
            binding.treatmentsContainer?.error = if (error) "Invalid treatment!" else null
        })
        // Price
        binding.price.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWorkDayExecTreatmentViewModel.saveTreatment()
            }
            false
        }
        addWorkDayExecTreatmentViewModel.priceError.observe(viewLifecycleOwner, Observer { error ->
            binding.priceContainer.error = if (error) "Invalid price!" else null
        })
        // Updated event
        addWorkDayExecTreatmentViewModel.workDayExecTreatmentUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        // Snackbar
        addWorkDayExecTreatmentViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    override fun onResumeAuthenticated() {
        addWorkDayExecTreatmentViewModel.start(workDayId, executedTreatmentId)
    }
}
