package com.davidmiguel.godentist.manageworkdays.add

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
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.datepicker.MaterialDatePicker
import com.davidmiguel.godentist.core.R as RC

class AddWorkDayFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddWorkDayBinding
    private val addWorkDayViewModel: AddWorkDayViewModel
            by viewModels { ViewModelFactory.getInstance() }
    private val clinicsAdapter
            by lazy { AddWorkDayClinicsAdapter(requireContext()) }
    private lateinit var datePicker: MaterialDatePicker<Long>

    private var workDayId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentAddWorkDayBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addWorkDayViewModel
            clinic.setAdapter(clinicsAdapter)
            return root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDatePicker()
        setupViewModelListeners()
    }

    private fun setupDatePicker() {
        datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(addWorkDayViewModel.date.value)
            .build()
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            addWorkDayViewModel.date.value = selectedDate
        }
    }

    private fun setupViewModelListeners() {
        // Footer btn
        requireMainActivity().showFAB(
            RC.drawable.ic_done_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            addWorkDayViewModel.saveWorkDay()
        }
        // Date
        binding.date.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.toString())
        }
        addWorkDayViewModel.dateError.observe(viewLifecycleOwner, Observer { error ->
            binding.dateContainer.error = if (error) "Invalid date!" else null
        })
        // Clinic
        addWorkDayViewModel.clinics.observe(viewLifecycleOwner, Observer { clinics ->
            clinicsAdapter.setClinics(clinics)
        })
        binding.clinic.setOnItemClickListener { _, _, position, _ ->
            addWorkDayViewModel.clinic.value = clinicsAdapter.getItem(position)
        }
        addWorkDayViewModel.clinicError.observe(viewLifecycleOwner, Observer { error ->
            binding.clinicContainer?.error = if (error) "Invalid clinic!" else null
        })
        // Duration
        binding.duration.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWorkDayViewModel.saveWorkDay()
            }
            false
        }
        addWorkDayViewModel.durationError.observe(viewLifecycleOwner, Observer { error ->
            binding.durationContainer.error = if (error) "Invalid duration!" else null
        })
        // Updated event
        addWorkDayViewModel.workDayUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        // Snackbar
        addWorkDayViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    override fun onResumeAuthenticated() {
        addWorkDayViewModel.start(workDayId)
    }
}
