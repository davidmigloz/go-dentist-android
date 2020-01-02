package com.davidmiguel.godentist.managetreatments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davidmiguel.godentist.core.R
import com.davidmiguel.godentist.core.base.AuthenticatedFragment
import com.davidmiguel.godentist.core.utils.observeEvent
import com.davidmiguel.godentist.managetreatments.ViewModelFactory
import com.davidmiguel.godentist.managetreatments.databinding.FragmentAddTreatmentBinding
import com.davidmiguel.godentist.requireMainActivity
import com.google.android.material.bottomappbar.BottomAppBar

class AddTreatmentFragment : AuthenticatedFragment() {

    private lateinit var binding: FragmentAddTreatmentBinding
    private val addTreatmentViewModel: AddTreatmentViewModel
            by viewModels { ViewModelFactory.getInstance() }
    private var treatmentId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        FragmentAddTreatmentBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            vm = addTreatmentViewModel
            return root
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelListeners()
    }

    private fun setupViewModelListeners() {
        requireMainActivity().showFAB(
            R.drawable.ic_done_black_24dp,
            BottomAppBar.FAB_ALIGNMENT_MODE_END
        ) {
            addTreatmentViewModel.saveTreatment()
        }
        binding.name.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTreatmentViewModel.saveTreatment()
            }
            false
        }
        addTreatmentViewModel.nameError.observe(viewLifecycleOwner, Observer { error ->
            binding.nameContainer.error = if (error) "Invalid name!" else null
        })
        addTreatmentViewModel.treatmentUpdatedEvent.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        addTreatmentViewModel.snackbarEvent.observeEvent(viewLifecycleOwner) { msg ->
            requireMainActivity().showSnackbar(msg)
        }
    }

    override fun onResumeAuthenticated() {
        addTreatmentViewModel.start(treatmentId)
    }
}
