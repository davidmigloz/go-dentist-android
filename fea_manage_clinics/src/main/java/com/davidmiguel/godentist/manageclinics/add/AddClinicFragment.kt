package com.davidmiguel.godentist.manageclinics.add

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
import com.davidmiguel.godentist.manageclinics.ViewModelFactory
import com.davidmiguel.godentist.manageclinics.databinding.FragmentAddClinicBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.davidmiguel.godentist.core.R as RC

class AddClinicFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddClinicBinding
    private val addClinicViewModel: AddClinicViewModel
            by viewModels { ViewModelFactory.getInstance() }
    private var clinicId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        FragmentAddClinicBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addClinicViewModel
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
            addClinicViewModel.saveClinic()
        }
        binding.percentage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addClinicViewModel.saveClinic()
            }
            false
        }
        addClinicViewModel.nameError.observe(viewLifecycleOwner, Observer { error ->
            binding.nameContainer.error = if (error) "Invalid name!" else null
        })
        addClinicViewModel.percentageError.observe(viewLifecycleOwner, Observer { error ->
            binding.percentageContainer.error = if (error) "Invalid percentage!" else null
        })
        addClinicViewModel.clinicUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        addClinicViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    override fun onResumeAuthenticated() {
        addClinicViewModel.start(clinicId)
    }
}
