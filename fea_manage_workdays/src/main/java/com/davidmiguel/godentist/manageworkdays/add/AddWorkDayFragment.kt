package com.davidmiguel.godentist.manageworkdays.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.model.Clinic
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.manageworkdays.R
import com.davidmiguel.godentist.manageworkdays.ViewModelFactory
import com.davidmiguel.godentist.manageworkdays.databinding.FragmentAddWorkDayBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class AddWorkDayFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddWorkDayBinding
    private val addWorkDayViewModel: AddWorkDayViewModel
            by viewModels { ViewModelFactory.getInstance() }
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
            return root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showFAB(
            RC.drawable.ic_done_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            addWorkDayViewModel.saveWorkDay()
        }
        addWorkDayViewModel.clinics.observe(viewLifecycleOwner, Observer { clinics ->
            setupClinicsSpinner(clinics)
        })
        binding.duration.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWorkDayViewModel.saveWorkDay()
            }
            false
        }
        addWorkDayViewModel.durationError.observe(viewLifecycleOwner, Observer { error ->
            binding.durationContainer.error = if (error) "Invalid name!" else null
        })
        addWorkDayViewModel.workDayUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        addWorkDayViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    private fun setupClinicsSpinner(clinics: List<Clinic>) {
        binding.clinic.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.fragment_add_work_day_clinic,
                clinics
            )
        )
    }

    override fun onResumeAuthenticated() {
        addWorkDayViewModel.start(workDayId)
    }
}
