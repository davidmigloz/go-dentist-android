package com.davidmiguel.godentist.manageworkdays.addworkday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.SwipeToDeleteCallback
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageworkdays.ViewModelFactory
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.datepicker.MaterialDatePicker
import com.davidmiguel.godentist.core.R as RC

class AddWorkDayFragment : AuthenticatedFragment() {

    private val args: AddWorkDayFragmentArgs by navArgs()
    private lateinit var binding: FragmentAddWorkDayBinding
    private val addWorkDayViewModel: AddWorkDayViewModel
            by navGraphViewModels(RC.id.add_work_day_nav_graph) { ViewModelFactory.getInstance() }
    private val clinicsAdapter
            by lazy { AddWorkDayClinicsAdapter(requireContext()) }
    private lateinit var datePicker: MaterialDatePicker<Long>

    private var workDayId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workDayId = args.workDayId
        FragmentAddWorkDayBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addWorkDayViewModel
            initContent()
            return root
        }
    }

    private fun initContent() {
        binding.clinic.apply {
            setAdapter(clinicsAdapter)
        }
        binding.treatmentsList.apply {
            adapter = AddWorkDayTreatmentsAdapter()
            val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = adapter as AddWorkDayTreatmentsAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            })
            itemTouchHelper.attachToRecyclerView(this)
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
            binding.dateContainer.error =
                if (error) getString(RC.string.addWorkDay_errorDate) else null
        })
        // Duration
        addWorkDayViewModel.durationError.observe(viewLifecycleOwner, Observer { error ->
            binding.durationContainer.error =
                if (error) getString(RC.string.addWorkDay_errorDuration) else null
        })
        // Clinic
        addWorkDayViewModel.clinics.observe(viewLifecycleOwner, Observer { clinics ->
            clinicsAdapter.setClinics(clinics)
        })
        binding.clinic.setOnItemClickListener { _, _, position, _ ->
            addWorkDayViewModel.clinic.value = clinicsAdapter.getItem(position)
        }
        addWorkDayViewModel.clinicError.observe(viewLifecycleOwner, Observer { error ->
            binding.clinicContainer?.error =
                if (error) getString(RC.string.addWorkDay_errorClinic) else null
        })
        // Notes
        addWorkDayViewModel.notesError.observe(viewLifecycleOwner, Observer { error ->
            binding.notesContainer.error =
                if (error) getString(RC.string.addWorkDay_errorNotes) else null
        })
        // Add exec treatment event
        addWorkDayViewModel.addWorkDayExecTreatmentEvent.observeEvent(viewLifecycleOwner) { executedTreatmentId ->
            findNavController().navigate(
                AddWorkDayFragmentDirections.actionAddWorkDayFragmentToAddWorkDayExecTreatmentFragment(
                    if (executedTreatmentId.isBlank()) null else executedTreatmentId
                )
            )
        }
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
        addWorkDayViewModel.startAddWorkDay(workDayId)
    }
}
